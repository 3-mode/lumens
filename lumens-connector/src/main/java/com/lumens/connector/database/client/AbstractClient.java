/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.database.Client;
import com.lumens.connector.database.DbUtils;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;

/**
 *
 * @author shaofeng wang
 */
public abstract class AbstractClient implements Client
{
    protected URLClassLoader driverLoader;
    protected Driver driver;
    protected Connection conn;
    protected String connURL;
    protected String user;
    protected String password;

    public AbstractClient(String driverURL, String driverClass, String connURL, String user,
                          String password)
    {
        try
        {
            driver = (Driver) DbUtils.getInstance(driverURL, driverClass);
            this.connURL = connURL;
            this.user = user;
            this.password = password;
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
