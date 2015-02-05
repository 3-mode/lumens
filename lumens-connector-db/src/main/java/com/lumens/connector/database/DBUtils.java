/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DBUtils {

    private static final Map<String, ClassLoader> driverClassMap = new HashMap<>();

    public static void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static Connection getConnection(Driver driver, String connURL, String user, String password) {
        try {
            // TODO check null ?
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);
            Connection conn = driver.connect(connURL, props);
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void releaseResultSet(ResultSet ret) {
        if (ret != null) {
            try {
                ret.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void releaseStatement(Statement stat) {
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static Object getInstance(String jarURL, String className) throws Exception {
        ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            // Try to instance if fail then try to load the jar first
            Class clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (ClassNotFoundException ce) {
            ClassLoader urlClassLoader = driverClassMap.get(jarURL);
            if (urlClassLoader == null) {
                urlClassLoader = new URLClassLoader(new URL[]{
                    new URL(jarURL)
                }, savedClassLoader);
                driverClassMap.put(jarURL, urlClassLoader);
            }
            Class<?> clazz = Class.forName(className, true, urlClassLoader);
            return clazz.newInstance();
        }
    }

    public static byte[] toByteArray(InputStream binaryStream) throws IOException {
        try {
            return IOUtils.toByteArray(binaryStream);
        } finally {
            IOUtils.closeQuietly(binaryStream);
        }
    }

    public static void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void commit(Connection conn) {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
