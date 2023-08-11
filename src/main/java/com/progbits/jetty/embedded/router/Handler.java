package com.progbits.jetty.embedded.router;

/**
 *
 * @author scarr
 */
@FunctionalInterface
public interface Handler {
    void handle(JettyEmbeddedRequest req, JettyEmbeddedResponse resp) throws Exception;
}
