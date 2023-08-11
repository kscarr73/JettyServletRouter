package com.progbits.jetty.embedded.util;

import com.progbits.api.exception.ApiClassNotFoundException;
import com.progbits.api.exception.ApiException;
import com.progbits.api.model.ApiObject;
import com.progbits.api.parser.JsonObjectParser;
import com.progbits.api.writer.JsonObjectWriter;
import com.progbits.api.writer.YamlObjectWriter;
import com.progbits.jetty.embedded.execeptions.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author scarr
 */
public class HttpReqHelper {

    private static final Logger log = LogManager.getLogger(HttpReqHelper.class);

    public static JsonObjectWriter jsonWriter = new JsonObjectWriter(true);
    public static YamlObjectWriter yamlWriter = new YamlObjectWriter(true);
    public static JsonObjectParser jsonParser = new JsonObjectParser(true);

    public static ApiObject pullReqParams(HttpServletRequest req) {
        ApiObject retObj = new ApiObject();

        req.getParameterMap().forEach((k, v) -> {
            if (v.length > 1) {
                retObj.put(k, Arrays.asList(v));
            } else {
                retObj.put(k, v[0]);
            }
        });

        return retObj;
    }

    public static ApiObject processJsonHttpPayload(HttpServletRequest req) throws ApplicationException {
        try {
            return jsonParser.parseSingle(req.getReader());
        } catch (IOException iex) {
            throw new ApplicationException(400, iex.getMessage());
        } catch (ApiClassNotFoundException | ApiException aex) {
            throw new ApplicationException(500, aex.getMessage());
        }
    }

    public static void sendJson(HttpServletResponse resp, int status, ApiObject json) {
        try {
            sendString(resp, status, "application/json", jsonWriter.writeSingle(json));
        } catch (ApplicationException | ApiException aex) {
            log.error("Parsing Error", aex);
        }
    }

    public static void sendYaml(HttpServletResponse resp, int status, ApiObject json) {
        try {
            sendString(resp, status, "application/x-yaml", yamlWriter.writeSingle(json));
        } catch (ApplicationException | ApiException aex) {
            log.error("Parsing Error", aex);
        }
    }

    public static void sendString(HttpServletResponse resp, int status, String contentType, String subject) throws ApplicationException {
        try {
            resp.setStatus(status);
            resp.setContentType(contentType);
            resp.setContentLength(subject.length());
            resp.getWriter().append(subject);
        } catch (IOException aex) {
            throw new ApplicationException(400, aex.getMessage());
        }
    }

    public static void writeFile(InputStream is, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096]; // tweaking this number may increase performance  
        int len;

        while ((len = is.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        out.flush();
    }
}

