package com.progbits.jetty.embedded.router;

import com.progbits.api.model.ApiObject;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author scarr
 */
public class StringMatchRoute implements Route {

    private String method;
    private String path;
    private Handler handler;
    private String payloadType;
    private String responseType;
    private AuthenticationHandler authHandler = null;

    public StringMatchRoute(String method, String path, Handler handler, String payloadType, String responseType, AuthenticationHandler authHandler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
        this.payloadType = payloadType;
        this.responseType = responseType;
        this.authHandler = authHandler;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public AuthenticationHandler getAuthHandler() {
        return authHandler;
    }

    @Override
    public ApiObject matches(HttpServletRequest req) {
        ApiObject objRet = new ApiObject();
        objRet.setString(ServletRouter.FIELD_PAYLOADTYPE, payloadType);
        objRet.setString(ServletRouter.FIELD_RESPONSETYPE, responseType);

        objRet.setBoolean(ServletRouter.FIELD_MATCHES, method.equals(req.getMethod()) && path.equals(req.getRequestURI()));

        return objRet;
    }

    public static StringMatchRouteBuilder builder() {
        return new StringMatchRouteBuilder();
    }

    public static class StringMatchRouteBuilder {

        private String method;
        private String path;
        private String payloadType = null;
        private String responseType = null;
        private AuthenticationHandler authHandler = null;

        private Handler handler;

        public StringMatchRouteBuilder method(String method) {
            this.method = method;

            return this;
        }

        public StringMatchRouteBuilder path(String path) {
            this.path = path;

            return this;
        }

        public StringMatchRouteBuilder handler(Handler handler) {
            this.handler = handler;

            return this;
        }

        public StringMatchRouteBuilder payloadType(String payloadType) {
            this.payloadType = payloadType;

            return this;
        }

        public StringMatchRouteBuilder responseType(String responseType) {
            this.responseType = responseType;

            return this;
        }

        public StringMatchRouteBuilder authHandler(AuthenticationHandler authHandler) {
            this.authHandler = authHandler;

            return this;
        }

        public StringMatchRoute build() {
            return new StringMatchRoute(method, path, handler, payloadType, responseType, authHandler);
        }
    }
}
