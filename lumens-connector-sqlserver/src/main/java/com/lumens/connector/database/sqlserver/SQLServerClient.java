/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.sqlserver;

import com.lumens.connector.database.client.AbstractClient;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author shaofeng wang
 */
public class SQLServerClient extends AbstractClient implements SQLServerConstants {

    public SQLServerClient(SQLServerConnector connector) {
        super(connector, SQLSERVER_CLASS);
    }

    @Override
    protected Type toType(String dataType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getTableNameQuerySQL() {
        return SQLSERVER_TABLENAMES;
    }

    @Override
    protected String getTableColumnQuerySQL() {
        return SQLSERVER_TABLECOLUMNS;
    }

    @Override
    protected Format createTableFormat(ResultSet ret) throws SQLException {
        String tableName = ret.getString(1);
        Long tableId = ret.getLong(2);
        String tableXType = ret.getString(3);
        Format table = new DataFormat(tableName, Format.Form.STRUCT);
        table.setProperty(SQLSERVER_ID, new Value(tableId));
        table.setProperty(SQLSERVER_XTYPE, new Value(tableXType));
        return table;
    }
}
