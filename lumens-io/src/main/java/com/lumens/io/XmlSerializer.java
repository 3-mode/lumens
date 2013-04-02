/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface XmlSerializer
{
    public void read(InputStream in) throws Exception;

    public void write(OutputStream out) throws Exception;
}
