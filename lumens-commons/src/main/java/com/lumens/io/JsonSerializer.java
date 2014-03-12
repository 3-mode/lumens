/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author shaofeng wang (shaofeng.wang@outlook.com)
 */
public interface JsonSerializer {

    public void readFromJson(InputStream in) throws Exception;

    public void writeToJson(OutputStream out) throws Exception;
}
