package com.progbits.jetty.embedded.router;

/**
 *
 * @author scarr
 */
public class RouteBuilder {

    public static Route createRoute(String method, String path, Handler handler, String payloadType, String responseType) {
        if (path.contains("${")) {
            return PathMatchRoute.builder()
                    .method(method)
                    .path(path)
                    .handler(handler)
                    .payloadType(payloadType)
                    .responseType(responseType)
                    .build();
        } else {
            return StringMatchRoute.builder()
                    .method(method)
                    .path(path)
                    .handler(handler)
                    .payloadType(payloadType)
                    .responseType(responseType)
                    .build();
        }
    }

    public static Route createRouteWithAuth(String method, String path, Handler handler, String payloadType, String responseType, AuthenticationHandler authHandler) {
        if (path.contains("${")) {
            return PathMatchRoute.builder()
                    .method(method)
                    .path(path)
                    .handler(handler)
                    .payloadType(payloadType)
                    .responseType(responseType)
                    .authHandler(authHandler)
                    .build();
        } else {
            return StringMatchRoute.builder()
                    .method(method)
                    .path(path)
                    .handler(handler)
                    .payloadType(payloadType)
                    .responseType(responseType)
                    .authHandler(authHandler)
                    .build();
        }
    }
}
