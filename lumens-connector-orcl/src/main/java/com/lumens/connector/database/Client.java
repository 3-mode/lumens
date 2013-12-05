/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface Client {

    public void open();

    public void close();

    public Map<String, Format> getFormatList(boolean fullLoad);

    public Format getFormat(Format format);
}
