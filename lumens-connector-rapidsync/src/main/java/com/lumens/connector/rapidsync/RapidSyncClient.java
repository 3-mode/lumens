/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapidsync;

import com.lumens.connector.database.client.AbstractClient;
import com.lumens.connector.database.client.DBConnector;
import com.lumens.model.Format;
import com.lumens.model.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapidSyncClient extends AbstractClient {

    public RapidSyncClient(DBConnector connector, String driverClass) {
        super(connector, driverClass);
    }

    @Override
    protected Type toType(String dataType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getTableNameQuerySQL() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getTableColumnQuerySQL() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Format createTableFormat(ResultSet ret) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ResultSet executeGetResult(String sql) {
        try (Statement stat = conn.createStatement();
                ResultSet ret = stat.executeQuery(sql)) {
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(sql, e);
        }
    }
}
