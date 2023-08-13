# JettyServletRouter

A small class to create an Embedded Jetty that can serve jakarta.servlet classes.

[Reference Implementation](https://github.com/kscarr73/JettyEmbedded_Reference)

The main class is the ServletRouter.  This class is your starting point to create a router set, for your project.

Create implementations of `com.progbits.jetty.embedded.servlet.ServletController` and add them to the ServletRouter.  This will call seperate Controller, based on the paths.

The ServletRouter has defaults to processing JSON, and can parse inbound payloads from JSON and processes Response ApiObject into JSON as well.

## Simple Example

```java
public class MainApplication {
   public static void main(String[] args) {
        ServletRouter routes = new ServletRouter();

        routes.addServletController(new BaseController());

        JettyEmbedded.builder()
            .setContextPath(CONTEXT_PATH)
            .setPort(8080)
            .useServletRoutes(routes)
            .build();
    }

}

public class BaseController implements ServletController {

    @Override
    public void init() {
        // Nothing to do yet
    }

    @Override
    public void routes(ServletRouter sr) {
        sr.get(MainApplication.CONTEXT_PATH + "/healthcheck", this::healthCheck);
    }
    
    private void healthCheck(JettyEmbeddedRequest req, JettyEmbeddedResponse resp) throws Exception {
        ApiObject objResp = new ApiObject();
        
        objResp.setString("message", "Hello World");
        
        resp.payload(objResp);
    }
```
