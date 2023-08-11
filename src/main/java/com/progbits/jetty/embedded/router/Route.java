package com.progbits.jetty.embedded.router;

import com.progbits.api.model.ApiObject;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author scarr
 */
public interface Route {
    public Handler getHandler();
    
    public ApiObject matches(HttpServletRequest req);
}
