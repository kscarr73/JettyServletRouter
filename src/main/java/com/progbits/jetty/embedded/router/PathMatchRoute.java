package com.progbits.jetty.embedded.router;

import com.progbits.api.model.ApiObject;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author scarr
 */
public class PathMatchRoute implements Route {
    private String method;
    
    private Pattern path;
    private List<String> pathNames;
    private List<String> pathTypes;
    private Handler handler;
    private String payloadType;
    private String responseType;
    
    public PathMatchRoute(String method, Pattern path, List<String> pathNames, List<String> pathTypes, Handler handler, String payloadType, String responseType) {
        this.method = method;
        this.path = path;
        this.pathNames = pathNames;
        this.pathTypes = pathTypes;
        this.handler = handler;
        this.payloadType = payloadType;
        this.responseType = responseType;
    }
    
    @Override
    public Handler getHandler() {
        return handler;
    }
    
    @Override
    public ApiObject matches(HttpServletRequest req) {
        ApiObject objRet = new ApiObject();
        objRet.setString(ServletRouter.FIELD_PAYLOADTYPE, payloadType);
        objRet.setString(ServletRouter.FIELD_RESPONSETYPE, responseType);
        
        boolean matches = method.equals(req.getMethod());
        
        if (matches) {
            Matcher matcher = path.matcher(req.getRequestURI());
            
            if (matcher.matches()) {
                objRet.setBoolean(ServletRouter.FIELD_MATCHES, true);
                
                for (int x = 1; x <= matcher.groupCount(); x++) {
                    String strValue = matcher.group(x);
                    
                    if ("int".equals(pathTypes.get(x - 1))) {
                        objRet.setInteger(pathNames.get(x - 1), Integer.valueOf(strValue));
                    } else {
                        objRet.setString(pathNames.get(x - 1), strValue);
                    }
                }
            } else {
                objRet.setBoolean(ServletRouter.FIELD_MATCHES, false);
            }
        }
        
        return objRet;
    }    
    
    public static PathMatchRouteBuilder builder() {
        return new PathMatchRouteBuilder();
    }
    
    public static class PathMatchRouteBuilder {
        private String method;
        private String path;
        private final List<String> pathNames = new ArrayList<>();
        private final List<String> pathTypes = new ArrayList<>();
        private String payloadType = null;
        private String responseType = null;
        
        private Handler handler;
        
        public PathMatchRouteBuilder method(String method) {
            this.method = method;
            
            return this;
        }
        
        public PathMatchRouteBuilder path(String path) {
            this.path = path;
            
            return this;
        }
        
        public PathMatchRouteBuilder handler(Handler handler) {
            this.handler = handler;
            
            return this;
        }
        
        public PathMatchRouteBuilder payloadType(String payloadType) {
            this.payloadType = payloadType;
            
            return this;
        }
        
        public PathMatchRouteBuilder responseType(String responseType) {
            this.responseType = responseType;
            
            return this;
        }
        
        private final Pattern patternSearch = Pattern.compile("\\$\\{(.*?)\\}");
        
        public PathMatchRoute build() {
            Pattern newPath = Pattern.compile(processPath());
            
            return new PathMatchRoute(method, newPath, pathNames, pathTypes, handler, payloadType, responseType);
        }
        
        private String processPath() {
            Matcher pathSearch = patternSearch.matcher(path);
            
            while (pathSearch.find()) {
                String varName = pathSearch.group(1);
                
                if (varName.contains(":")) {
                    String[] varNameSplit = varName.split(":");
                    
                    pathNames.add(varNameSplit[0]);
                    pathTypes.add(varNameSplit[1]);
                } else {
                    pathNames.add(varName);
                    pathTypes.add("str");
                }
            }
            
            return path.replaceAll("\\$\\{.*?\\}", "(.*?)");
        }
    }
}
