/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author shaofeng wang
 */
public class DbUtils
{
    public static void releaseConnection(Connection conn)
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException ex)
            {
            }
        }
    }

    public static Connection getConnection(Driver driver, String connURL, String user,
                                           String password)
    {
        try
        {
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);
            return driver.connect(connURL, props);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static void releaseResultSet(ResultSet ret)
    {
        if (ret != null)
        {
            try
            {
                ret.close();
            }
            catch (SQLException ex)
            {
            }
        }
    }

    public static void releaseStatement(Statement stat)
    {
        if (stat != null)
        {
            try
            {
                stat.close();
            }
            catch (SQLException ex)
            {
            }
        }
    }

    public static Object getInstance(String jarURL, String className) throws Exception
    {
        ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
        try
        {
            ClassLoader urlClassLoader = new URLClassLoader(new URL[]
                    {
                        new URL(jarURL)
                    });
            Thread.currentThread().setContextClassLoader(urlClassLoader);
            Class clazz = Class.forName(className, true, urlClassLoader);
            return clazz.newInstance();
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(savedClassLoader);
        }
    }
}