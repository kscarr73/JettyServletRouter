package com.progbits.jetty.embedded.servlet;

import com.progbits.jetty.embedded.execeptions.ApplicationException;
import com.progbits.jetty.embedded.router.JettyEmbeddedResponse;

/**
 *
 * @author scarr
 */
public interface ContentHandler {
    void processResponse(JettyEmbeddedResponse resp) throws ApplicationException;
}
