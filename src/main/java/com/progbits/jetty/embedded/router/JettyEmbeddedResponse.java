package com.progbits.jetty.embedded.router;

import com.progbits.api.model.ApiObject;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author scarr
 */
public class JettyEmbeddedResponse {
    private HttpServletResponse response;
    private ApiObject objPayload;
    private String contentType;
    private String strPayload;
    private Integer status = 200;
    
    public JettyEmbeddedResponse(HttpServletResponse response) {
        this.response = response;
    }
    
    public HttpServletResponse getResponse() {
        return response;
    }
    
    public JettyEmbeddedResponse contentLength(Integer length) {
        response.setContentLength(length);
        return this;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public JettyEmbeddedResponse status(Integer status) {
        this.status = status;
        
        return this;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public JettyEmbeddedResponse contentType(String contentType) {
        this.contentType = contentType;
        
        return this;
    }
    
    public JettyEmbeddedResponse addHeader(String header, String value) {
        response.addHeader(header, value);
        return this;
    }
    
    public JettyEmbeddedResponse payload(ApiObject obj) {
        this.objPayload = obj;
        return this;
    }
    
    public JettyEmbeddedResponse payload(String payload) {
        this.strPayload = payload;
        return this;
    }
    
    public ApiObject getApiPayload() {
        return objPayload;
    }
    
    public String getStringPayload() {
        return strPayload;
    }
}
