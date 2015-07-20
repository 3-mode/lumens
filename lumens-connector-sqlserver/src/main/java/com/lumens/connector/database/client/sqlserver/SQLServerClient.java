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
        if (dataType.equalsIgnoreCase(CHAR)
                || dataType.equalsIgnoreCase(VARCHAR)
                || dataType.equalsIgnoreCase(TEXT)
                || dataType.equalsIgnoreCase(NCHAR)
                || dataType.equalsIgnoreCase(NVARCHAR)
                || dataType.equalsIgnoreCase(NTEXT)) {
            return Type.STRING;
        } else if (dataType.equalsIgnoreCase(DATETIME)
                || dataType.equalsIgnoreCase(SMALLDATETIME)
                || dataType.equalsIgnoreCase(DATE)
                || dataType.equalsIgnoreCase(TIME)
                || dataType.equalsIgnoreCase(DATATIMEOFFSET)
                || dataType.equalsIgnoreCase(DATETIME2)) {
            return Type.DATE;
        } else if (dataType.equalsIgnoreCase(BINARY)
                || dataType.equalsIgnoreCase(IMAGE)
                || dataType.equalsIgnoreCase(VARBINARY)
                || dataType.equalsIgnoreCase(TIMESTAMP)
                || dataType.equalsIgnoreCase(ROWVERSION)) {
            return Type.BINARY;
        } else if (dataType.equalsIgnoreCase(FLOAT)
                || dataType.equalsIgnoreCase(REAL)
                || dataType.equalsIgnoreCase(SMALLMONEY)
                || dataType.equalsIgnoreCase(MONEY)) {
            return Type.DOUBLE;
        } else if (dataType.equalsIgnoreCase(BIGINT)) {
            return Type.LONG;
        } else if (dataType.equalsIgnoreCase(BIT)
                || dataType.equalsIgnoreCase(DECIMAL)
                || dataType.equalsIgnoreCase(INT)
                || dataType.equalsIgnoreCase(NUMERIC)
                || dataType.equalsIgnoreCase(SMALLINT)
                || dataType.equalsIgnoreCase(TINYINT)) {
            return Type.INTEGER;
        }
        return Type.NONE;
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
                if (primaryKeyList.length() > 0) {
                    primaryKeyList.append(", ");
                }
                primaryKeyList.append(primaryKeyRet.getString(1));
            }
            if (primaryKeyList.length() > 0) {
                table.setProperty(SQLSERVER_PK, new Value(primaryKeyList.toString()));
            }
        }

        return table;
    }
}
