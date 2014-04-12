/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.io.JsonUtility;
import java.util.UUID;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;

public class ServerUtils {

    public static String generateID(String prefix) {
        return prefix + '-' + UUID.randomUUID().toString();
    }

    public static Response getErrorMessageResponse(String error) {
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "Failed");
            json.writeStringField("error_message", error);
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Response getErrorMessageResponse(Exception ex) {
        return getErrorMessageResponse(ex.toString());
    }
}
