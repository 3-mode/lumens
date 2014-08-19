/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.io.JsonUtility;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;

public class ServerUtils {

    public static long generateID() {
        return System.currentTimeMillis();
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
        return getErrorMessageResponse(ex.toString());
    }
}
