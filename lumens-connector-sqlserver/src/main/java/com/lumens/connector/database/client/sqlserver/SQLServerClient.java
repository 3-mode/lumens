/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.database.client.AbstractClient;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        if (dataType.contains("int"))
            return Type.INTEGER;
        else if (dataType.contains("char") || dataType.contains("text"))
            return Type.STRING;

        return Type.STRING;
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
        try (Statement stat = conn.createStatement();
             ResultSet primaryKeyRet = stat.executeQuery(String.format(SQLSERVER_PRIMARYKEY, tableName))) {
            StringBuilder primaryKeyList = new StringBuilder();
            while (primaryKeyRet.next()) {
                if (primaryKeyList.length() > 0)
                    primaryKeyList.append(", ");
                primaryKeyList.append(primaryKeyRet.getString(1));
            }
            if (primaryKeyList.length() > 0)
                table.setProperty(SQLSERVER_PK, new Value(primaryKeyList.toString()));
        }

        return table;
    }
}
