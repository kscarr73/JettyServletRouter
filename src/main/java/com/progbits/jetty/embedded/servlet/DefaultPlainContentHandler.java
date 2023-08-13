package com.progbits.jetty.embedded.servlet;

import com.progbits.jetty.embedded.execeptions.ApplicationException;
import com.progbits.jetty.embedded.router.JettyEmbeddedResponse;
import static com.progbits.jetty.embedded.router.ServletRouter.CONTENT_TYPE_PLAIN;
import com.progbits.jetty.embedded.util.HttpReqHelper;

/**
 *
 * @author scarr
 */
public class DefaultPlainContentHandler implements ContentHandler {

    @Override
    public void processResponse(JettyEmbeddedResponse resp) throws ApplicationException {
        HttpReqHelper.sendString(resp.getResponse(), resp.getStatus(), CONTENT_TYPE_PLAIN, resp.getStringPayload());
    }
    
}
