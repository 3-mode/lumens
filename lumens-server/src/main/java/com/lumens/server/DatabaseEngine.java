/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import java.sql.Connection;
import java.util.Properties;
import org.apache.derby.jdbc.EmbeddedDriver;

public class DatabaseEngine {

    private final static String protocol = "jdbc:derby:derbyDB;create=true";
    private EmbeddedDriver driver;
    private Properties props;

    public void initialize(Properties props) {
        try {
            this.props = props;
            driver = new EmbeddedDriver();
            Connection conn = driver.connect(protocol, this.props);
            conn.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}