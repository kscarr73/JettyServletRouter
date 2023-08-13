package com.progbits.jetty.embedded.router;

import com.progbits.jetty.embedded.execeptions.AuthenticationException;

/**
 *
 * @author scarr_jp
 */
public interface AuthenticationHandler {

    boolean processAuth(JettyEmbeddedRequest req) throws AuthenticationException;

}
