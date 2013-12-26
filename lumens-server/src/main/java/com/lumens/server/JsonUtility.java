/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;

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
}
