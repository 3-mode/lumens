/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.database.client.AbstractClient;
import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class SQLServerClient extends AbstractClient implements SQLServerConstants
{
    public SQLServerClient(String ojdbcURL, String connURL, String user, String password)
    {
        super(ojdbcURL, SQLSERVER_CLASS, connURL, user, password);
    }

    @Override
    public void open()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Format getFormat(Format format)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Format> getFormatList(boolean fullLoad)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
