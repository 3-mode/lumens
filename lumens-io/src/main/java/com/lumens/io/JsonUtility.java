/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.NullNode;

/**
 *
 * Wrapper JsonGenerator and OutputStream
 */
public class JsonUtility {

    private JsonGenerator json;
    private ByteArrayOutputStream baos;

    public JsonUtility(JsonGenerator json, ByteArrayOutputStream baos) {
        this.json = json;
        this.baos = baos;
    }

    public JsonGenerator getGenerator() {
        return this.json;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return this.baos;
    }

    public String toUTF8String() throws IOException {
        this.json.flush();
        return this.baos.toString("UTF-8");
    }

    public static JsonUtility createJsonUtility() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator json = JsonUtility.createJsonGenerator(baos);
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
}
