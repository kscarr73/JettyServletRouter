package com.progbits.jetty.embedded.servlet;

import com.progbits.jetty.embedded.router.ServletRouter;

/**
 * Used to Implement a Servlet processor for ServletRoutes
 * 
 * @author scarr
 */
public interface ServletController {
    /**
     * Initialize the Servlet Controller
     * 
     * This is ran when a new Servlet Controller is added to the Servlet Router
     */
    void init();
    
    /**
     * Add the Routes that this Servlet should process
     * 
     * @param route 
     */
    void routes(ServletRouter route);
}
