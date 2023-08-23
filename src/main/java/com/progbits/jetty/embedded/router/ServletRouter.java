package com.progbits.jetty.embedded.router;

import com.progbits.api.model.ApiObject;
import com.progbits.jetty.embedded.routing.ServletRoutes;
import com.progbits.jetty.embedded.servlet.ContentHandler;
import com.progbits.jetty.embedded.servlet.DefaultHtmlContentHandler;
import com.progbits.jetty.embedded.servlet.DefaultJsonContentHandler;
import com.progbits.jetty.embedded.servlet.DefaultPlainContentHandler;
import com.progbits.jetty.embedded.servlet.DefaultYamlContentHandler;
import com.progbits.jetty.embedded.servlet.ServletController;
import com.progbits.jetty.embedded.util.HttpReqHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.http.HttpMethod;

/**
 *
 * @author scarr
 */
public class ServletRouter implements ServletRoutes {

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_YAML = "application/x-yaml";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_PLAIN = "text/plain";

    public static final String FIELD_MATCHES = "matches";
    public static final String FIELD_PAYLOADTYPE = "payloadType";
    public static final String FIELD_RESPONSETYPE = "responseType";

    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_SEARCH = "SEARCH";

    private final List<ServletController> servletControllers = new ArrayList<>();
    private final List<Route> routes = new ArrayList<>();
    private final List<ExceptionHandler> errorHandlers = new ArrayList<>();
    private final Map<String, ContentHandler> contentHandlers = new HashMap<>();

    private String defaultRequestType = CONTENT_TYPE_JSON;
    private String defaultResponseType = CONTENT_TYPE_JSON;

    public ServletRouter() {
        contentHandlers.put(CONTENT_TYPE_JSON, new DefaultJsonContentHandler());
        contentHandlers.put(CONTENT_TYPE_YAML, new DefaultYamlContentHandler());
        contentHandlers.put(CONTENT_TYPE_HTML, new DefaultHtmlContentHandler());
        contentHandlers.put(CONTENT_TYPE_PLAIN, new DefaultPlainContentHandler());
    }

    public ServletRoutes setDefaultResponseType(String contentType) {
        defaultResponseType = contentType;

        return this;
    }

    public ServletRoutes setDefaultRequestType(String contentType) {
        defaultRequestType = contentType;

        return this;
    }

    public ServletRoutes addServletController(ServletController controller) {
        controller.init();
        controller.routes(this);
        servletControllers.add(controller);

        return this;
    }

    /**
     * You can override default JSON and YAML handlers with this function.
     *
     * @param contentType The content type to set.
     * @param handler
     * @return
     */
    public ServletRouter addContentHandler(String contentType, ContentHandler handler) {
        contentHandlers.put(contentType, handler);

        return this;
    }

    public List<ServletController> getServletControllers() {
        return servletControllers;
    }

    public ServletRoutes addErrorHandler(ExceptionHandler exceptionHandler) {
        errorHandlers.add(exceptionHandler);

        return this;
    }

    public ServletRoutes addRoute(Route route) {
        routes.add(route);

        return this;
    }

    public ServletRoutes get(String path, Handler handler) {
        routes.add(RouteBuilder.createRoute(HttpMethod.GET.asString(), path, handler, null, defaultResponseType));

        return this;
    }

    public ServletRoutes get(String path, Handler handler, String responseType) {
        routes.add(RouteBuilder.createRoute(HttpMethod.GET.asString(), path, handler, null, responseType));

        return this;
    }

    public ServletRoutes post(String path, Handler handler) {
        routes.add(RouteBuilder.createRoute(HttpMethod.POST.asString(), path, handler, defaultRequestType, defaultResponseType));

        return this;
    }

    public ServletRoutes post(String path, Handler handler, String contentType, String responseType) {
        routes.add(RouteBuilder.createRoute(HttpMethod.POST.asString(), path, handler, contentType, responseType));

        return this;
    }

    public ServletRoutes search(String path, Handler handler) {
        routes.add(RouteBuilder.createRoute(HttpMethod.SEARCH.asString(), path, handler, defaultRequestType, defaultResponseType));

        return this;
    }

    public ServletRoutes search(String path, Handler handler, String contentType, String responseType) {
        routes.add(RouteBuilder.createRoute(HttpMethod.SEARCH.asString(), path, handler, contentType, responseType));

        return this;
    }

    public ServletRoutes put(String path, Handler handler) {
        routes.add(RouteBuilder.createRoute(HttpMethod.PUT.asString(), path, handler, defaultRequestType, defaultResponseType));

        return this;
    }

    public ServletRoutes put(String path, Handler handler, String contentType, String responseType) {
        routes.add(RouteBuilder.createRoute(HttpMethod.PUT.asString(), path, handler, contentType, responseType));

        return this;
    }

    public ServletRoutes custom(String method, String path, Handler handler, String contentType, String responseType) {
        routes.add(RouteBuilder.createRoute(method, path, handler, contentType, responseType));

        return this;
    }

    @Override
    public boolean processRoutes(HttpServletRequest req, HttpServletResponse resp) {
        boolean bRet = false;

        for (var entry : routes) {
            ApiObject objMatch = entry.matches(req);

            if (objMatch.isSet(FIELD_MATCHES)) {
                bRet = true;

                try {
                    JettyEmbeddedRequest jettyReq = new JettyEmbeddedRequest(req, objMatch);

                    if (objMatch.isSet(FIELD_PAYLOADTYPE)) {
                        if (CONTENT_TYPE_JSON.equals(objMatch.getString(FIELD_PAYLOADTYPE))) {
                            jettyReq.setApiPayload(HttpReqHelper.processJsonHttpPayload(req));
                        }
                    } else {
                        ApiObject objParams = HttpReqHelper.pullReqParams(req);
                        objMatch.putAll(objParams);
                    }

                    JettyEmbeddedResponse jettyResp = new JettyEmbeddedResponse(resp);

                    if (entry.getAuthHandler() != null) {
                        if (!entry.getAuthHandler().processAuth(jettyReq, jettyResp)) {
                            // If processAuth returns false, then 
                            // return from function with true
                            // And stop processing, because we have done a redirect
                            return true;
                        }
                    }

                    entry.getHandler().handle(jettyReq, jettyResp);

                    if (objMatch.isSet(FIELD_RESPONSETYPE)) {
                        if (jettyResp.getBytePayload() != null) {
                            resp.setContentLength(jettyResp.getBytePayload().length);
                            resp.setContentType(objMatch.getString(FIELD_RESPONSETYPE));
                            resp.getOutputStream().write(jettyResp.getBytePayload());
                        } else {
                            if (contentHandlers.containsKey(objMatch.getString(FIELD_RESPONSETYPE))) {
                                contentHandlers.get(objMatch.getString(FIELD_RESPONSETYPE)).processResponse(jettyResp);
                            }
                        }

                    }
                } catch (Exception ex) {
                    processExceptionHandlers(ex, req, resp);
                }
                break;
            }
        }

        return bRet;
    }

    private void processExceptionHandlers(Throwable thr, HttpServletRequest req, HttpServletResponse resp) {
        for (var entry : errorHandlers) {
            if (entry.handle(thr, req, resp)) {
                break;
            }
        }
    }
}
