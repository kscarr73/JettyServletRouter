package com.progbits.jetty.embedded.servlet;

import com.progbits.jetty.embedded.router.ServletRouter;

/**
 *
 * @author scarr
 */
public interface ServletController {
    void init();
    
    void routes(ServletRouter route);
}
