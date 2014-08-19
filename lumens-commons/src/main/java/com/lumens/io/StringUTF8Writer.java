/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class StringUTF8Writer {

    private OutputStreamWriter osw;
    private OutputStream outputStream;

    public StringUTF8Writer(OutputStream outputStream) {
        try {
            this.outputStream = outputStream;
            this.osw = new OutputStreamWriter(outputStream, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public OutputStream getOutStream() {
        return outputStream;
    }

    public StringUTF8Writer print(String str) throws IOException {
        if (str != null) {
            osw.write(str);
            osw.flush();
        }
        return this;
    }

    public StringUTF8Writer println(String str) throws IOException {
        print(str);
        print("\n");
        return this;
    }
}
