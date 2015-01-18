/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import static com.lumens.connector.database.DBConstants.DESCRIPTION;
import static com.lumens.connector.database.DBConstants.TYPE;
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
public class OracleClient extends AbstractClient implements OracleConstants {

    public OracleClient(OracleConnector connector) {
        super(connector, ORACLE_CLASS);
    }

    @Override
    public void open() {
        super.open();
        String sessionAlter = ((OracleConnector) connector).getSessionAlter();
        if (sessionAlter != null && !sessionAlter.isEmpty()) {
            try (Statement stat = conn.createStatement()) {
                String[] alterList = sessionAlter.split("\n");
                for (String alter : alterList) {
                    alter = alter.trim();
                    if (!alter.isEmpty())
                        stat.execute(alter.trim());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected Type toType(String dataType) {
        if (CHAR.equalsIgnoreCase(dataType)
            || CLOB.equalsIgnoreCase(dataType)
            || dataType.startsWith(VARCHAR2)
            || dataType.startsWith(NVARCHAR2)) {
            return Type.STRING;
        } else if (DATE.equalsIgnoreCase(dataType)) {
            return Type.DATE;
        } else if (BLOB.equalsIgnoreCase(dataType)) {
            return Type.BINARY;
        } else if (dataType.startsWith(NUMBERIC)) {
            return Type.DOUBLE;
        } else if (dataType.startsWith(NUMBER)) {
            return Type.INTEGER;
        }
        return Type.NONE;
    }

    @Override
    protected String getTableNameQuerySQL() {
        return TABLENAMES;
    }

    @Override
    protected String getTableColumnQuerySQL() {
        return TABLECOLUMNS;
    }

    @Override
    protected Format createTableFormat(ResultSet ret) throws SQLException {
        String tableName = ret.getString(1);
        String description = ret.getString(2);
        String type = ret.getString(3);
        Format table = new DataFormat(tableName, Format.Form.STRUCT);
        table.setProperty(DESCRIPTION, new Value(description));
        table.setProperty(TYPE, new Value(type));
        return table;
    }
}
