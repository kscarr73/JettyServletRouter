package com.progbits.jetty.embedded.servlet;

import com.progbits.jetty.embedded.execeptions.ApplicationException;
import com.progbits.jetty.embedded.router.JettyEmbeddedResponse;
import com.progbits.jetty.embedded.util.HttpReqHelper;

/**
 *
 * @author scarr
 */
public class DefaultJsonContentHandler implements ContentHandler {

    @Override
    public void processResponse(JettyEmbeddedResponse resp) throws ApplicationException {
        if (resp.getApiPayload() != null) {
            HttpReqHelper.sendJson(resp.getResponse(), resp.getStatus(), resp.getApiPayload());
        } else if (resp.getStringPayload() != null) {
            HttpReqHelper.sendString(resp.getResponse(), resp.getStatus(), resp.getContentType(), resp.getStringPayload());
        }
    }

}
