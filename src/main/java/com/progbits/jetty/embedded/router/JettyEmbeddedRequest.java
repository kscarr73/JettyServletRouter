package com.progbits.jetty.embedded.router;

import com.progbits.api.model.ApiObject;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author scarr
 */
public class JettyEmbeddedRequest {
    private HttpServletRequest request;
    private ApiObject reqInfo;
    private ApiObject apiPayload;
    private String strPayload;
    
    public JettyEmbeddedRequest(HttpServletRequest request, ApiObject reqInfo) {
        this.request = request;
        this.reqInfo = reqInfo;
    }
    
    public JettyEmbeddedRequest setReqInfo(ApiObject reqInfo) {
        this.reqInfo = reqInfo;
        
        return this;
    }
    
    public Integer getContentLength() {
        return request.getContentLength();
    }
    
    public String getContentType() {
        return request.getContentType();
    }
    
    public String getHeader(String headerName) {
        return request.getHeader(headerName);
    }
    
    public HttpServletRequest getRequest() {
        return request;
    }
    
    public ApiObject getRequestInfo() {
        return reqInfo;
    }
    
    public void setApiPayload(ApiObject payload) {
        this.apiPayload = payload;
    }
    
    public ApiObject getApiPayload() {
        return this.apiPayload;
    }
    
    public void setStrPayload(String payload) {
        this.strPayload = payload;
    }
    
    public String getStrPayload() {
        return strPayload;
    }
}
