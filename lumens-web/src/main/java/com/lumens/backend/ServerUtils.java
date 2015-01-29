/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.io.JsonUtility;
import java.io.File;
import java.nio.file.Paths;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;

public class ServerUtils {
    public static String getNormalizedPath(String path) {
        return Paths.get(new File(path).toURI()).normalize().toFile().getAbsolutePath();
    }

    public static Response getErrorMessageResponse(String error) {
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "Failed");
            json.writeStringField("error_message", error);
            json.writeEndObject();
            // TODO
            System.err.println(utility.toUTF8String());
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Response getErrorMessageResponse(Exception ex) {
        try {
            return getErrorMessageResponse(ex.toString());
        } finally {
            throw new RuntimeException(ex);
        }
    }
}
