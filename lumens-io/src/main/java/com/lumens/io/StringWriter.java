/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class StringWriter
{
    private DataOutputStream out;
    private OutputStream outputStream;

    public StringWriter(OutputStream outputStream)
    {
        this.outputStream = outputStream;
        this.out = new DataOutputStream(outputStream);
    }

    public OutputStream getOutStream()
    {
        return outputStream;
    }

    public StringWriter print(String str) throws IOException
    {
        if (str != null)
            out.writeBytes(str);
        return this;
    }

    public StringWriter println(String str) throws IOException
    {
        print(str);
        print("\n");
        return this;
    }
}
