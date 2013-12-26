/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.NullNode;

public class ServerUtils {

    public static JsonUtility createJsonUtility() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator json = ServerUtils.createJsonGenerator(baos);
        return new JsonUtility(json, baos);
    }

    public static JsonGenerator createJsonGenerator(OutputStream out) {
        try {
            ObjectMapper om = new ObjectMapper();
            return om.getJsonFactory().createJsonGenerator(out, JsonEncoding.UTF8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static JsonNode createJson(String jsonText) {
        try {
            ObjectMapper om = new ObjectMapper();
            return om.readTree(jsonText);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isNotNull(JsonNode json) {
        return json != null && json != NullNode.instance;
    }

    public static String generateID(String prefix) {
        return prefix + '-' + UUID.randomUUID().toString();
    }
}
