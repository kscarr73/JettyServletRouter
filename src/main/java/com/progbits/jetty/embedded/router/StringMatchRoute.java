package com.progbits.jetty.embedded.router;

import com.progbits.api.model.ApiObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author scarr
 */
@Data
@Builder
public class StringMatchRoute implements Route {
    private String method;
    private String path;
    private Handler handler;    
    private String payloadType;
    private String responseType;
    
    @Override
    public Handler getHandler() {
        return handler;
    }
    
    @Override
    public ApiObject matches(HttpServletRequest req) {
        ApiObject objRet = new ApiObject();
        objRet.setString(ServletRouter.FIELD_PAYLOADTYPE, payloadType);
        objRet.setString(ServletRouter.FIELD_RESPONSETYPE, responseType);
        
        objRet.setBoolean(ServletRouter.FIELD_MATCHES, method.equals(req.getMethod()) && path.equals(req.getRequestURI()));
        
        return objRet;
    }    
}
