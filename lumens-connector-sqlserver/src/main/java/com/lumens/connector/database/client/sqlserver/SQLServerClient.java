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
                || TEXT.equalsIgnoreCase(dataType)
                || SQL_VARIANT.equalsIgnoreCase(dataType) // There may be a bug here, as variant could also conver int, binary
                || UNIQUEIDENTIFIER.equalsIgnoreCase(dataType)
                || NTEXT.equalsIgnoreCase(dataType)) {
            return Type.STRING;
        } else if (DATETIME.equalsIgnoreCase(dataType)
                || SMALLDATETIME.equalsIgnoreCase(dataType)
                || DATE.equalsIgnoreCase(dataType)
                || dataType.startsWith(TIME)
                || dataType.startsWith(DATATIMEOFFSET)
                || dataType.startsWith(DATETIME2)) {
            return Type.DATE;
        } else if (dataType.startsWith(BINARY)
                || dataType.startsWith(VARBINARY)
                || IMAGE.equalsIgnoreCase(dataType)
                || TIMESTAMP.equalsIgnoreCase(dataType)
                || ROWVERSION.equalsIgnoreCase(dataType)
                || XML.equalsIgnoreCase(dataType)) {
            return Type.BINARY;
        } else if (FLOAT.equalsIgnoreCase(dataType)
                || REAL.equalsIgnoreCase(dataType)) {  // Convert to max in JAVA
            return Type.DOUBLE;
        } else if (dataType.startsWith(DECIMAL)
                || dataType.startsWith(NUMERIC)
                || SMALLMONEY.equalsIgnoreCase(dataType)
                || MONEY.equalsIgnoreCase(dataType)) {
            return Type.BIGDECIMAL;
        } else if (BIGINT.equalsIgnoreCase(dataType)) {
            return Type.LONG;
        } else if (BIT.equalsIgnoreCase(dataType)) {
            return Type.BOOLEAN;
        } else if (INT.equalsIgnoreCase(dataType)
                || INT2.equalsIgnoreCase(dataType)
                || INT2.equalsIgnoreCase(dataType)
                || INT4.equalsIgnoreCase(dataType)
                || SMALLINT.equalsIgnoreCase(dataType)
                || TINYINT.equalsIgnoreCase(dataType)
                || HIERARCHYID.equalsIgnoreCase(dataType)) {
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
