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
import java.math.BigDecimal;

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
        if (dataType.startsWith(CHAR)
                || dataType.startsWith(NCHAR)
                || dataType.startsWith(VARCHAR)
                || dataType.startsWith(NVARCHAR)
                || dataType.equalsIgnoreCase(TEXT)
                || dataType.equalsIgnoreCase(SQL_VARIANT) // There may be a bug here, as variant could also conver int, binary
                || dataType.equalsIgnoreCase(UNIQUEIDENTIFIER)
                || dataType.equalsIgnoreCase(NTEXT)) {
            return Type.STRING;
        } else if (dataType.equalsIgnoreCase(DATETIME)
                || dataType.equalsIgnoreCase(SMALLDATETIME)
                || dataType.equalsIgnoreCase(DATE)
                || dataType.startsWith(TIME)
                || dataType.startsWith(DATATIMEOFFSET)
                || dataType.startsWith(DATETIME2)) {
            return Type.DATE;
        } else if (dataType.startsWith(BINARY)
                || dataType.startsWith(VARBINARY)
                || dataType.equalsIgnoreCase(IMAGE)
                || dataType.equalsIgnoreCase(TIMESTAMP)
                || dataType.equalsIgnoreCase(ROWVERSION)
                || dataType.equalsIgnoreCase(XML)) {
            return Type.BINARY;
        } else if (dataType.equalsIgnoreCase(FLOAT)
                || dataType.equalsIgnoreCase(REAL)) {  // Convert to max in JAVA
            return Type.DOUBLE;
        } else if (dataType.startsWith(DECIMAL)
                || dataType.startsWith(NUMERIC)
                || dataType.equalsIgnoreCase(SMALLMONEY)
                || dataType.equalsIgnoreCase(MONEY)) {
            return Type.BIGDECIMAL;
        } else if (dataType.equalsIgnoreCase(BIGINT)) {
            return Type.LONG;
        } else if (dataType.equalsIgnoreCase(BIT)) {
            return Type.BOOLEAN;
        } else if (dataType.equalsIgnoreCase(INT)
                || dataType.equalsIgnoreCase(INT2)
                || dataType.equalsIgnoreCase(INT2)
                || dataType.equalsIgnoreCase(INT4)
                || dataType.equalsIgnoreCase(SMALLINT)
                || dataType.equalsIgnoreCase(TINYINT)
                || dataType.equalsIgnoreCase(HIERARCHYID)) {
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
