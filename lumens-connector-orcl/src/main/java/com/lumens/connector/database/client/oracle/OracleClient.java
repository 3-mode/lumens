/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.DbUtils;
import com.lumens.connector.database.client.AbstractClient;
import static com.lumens.connector.database.client.oracle.OracleConstants.DATA_LENGTH;
import static com.lumens.connector.database.client.oracle.OracleConstants.DATA_TYPE;
import static com.lumens.connector.database.client.oracle.OracleConstants.FIELDS;
import static com.lumens.connector.database.client.oracle.OracleConstants.TABLECOLUMNS;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class OracleClient extends AbstractClient implements OracleConstants {
    public OracleClient(String ojdbcURL, String connURL, String user, String password, String sessionAlter) {
        super(ojdbcURL, ORACLE_CLASS, connURL, user, password, sessionAlter);
    }

    @Override
    public void open() {
        conn = DbUtils.getConnection(driver, connURL, user, password);
        if (sessionAlter != null && !sessionAlter.isEmpty()) {
            Statement stat = null;
            try {
                stat = conn.createStatement();
                String[] alterList = sessionAlter.split("\n");
                for (String alter : alterList) {
                    alter = alter.trim();
                    if (!alter.isEmpty())
                        stat.execute(alter.trim());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                DbUtils.releaseStatement(stat);
            }
        }
    }

    @Override
    public void close() {
        DbUtils.releaseConnection(conn);
    }

    @Override
    public Map<String, Format> getFormatList(boolean fullLoad) {
        Map<String, Format> tables = new HashMap<String, Format>();
        Statement stat = null;
        PreparedStatement preparedStat = null;
        ResultSet ret = null;
        ResultSet preparedRet = null;
        try {
            stat = conn.createStatement();
            ret = stat.executeQuery(TABLENAMES);
            if (!ret.isClosed()) {
                while (ret.next()) {
                    String tableName = ret.getString(1);
                    String description = ret.getString(2);
                    String type = ret.getString(3);
                    Format table = new DataFormat(tableName, Form.STRUCT);
                    tables.put(tableName, table);
                    table.addChild(FIELDS, Form.STRUCT);
                    table.addChild(OPERATION, Form.FIELD, Type.STRING);
                    table.addChild(CLAUSE, Form.FIELD, Type.STRING);
                    table.setProperty(DESCRIPTION, new Value(description));
                    table.setProperty(TYPE, new Value(type));
                    if (fullLoad) {
                        getFormat(table);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.releaseResultSet(ret);
            DbUtils.releaseStatement(stat);
            DbUtils.releaseResultSet(preparedRet);
            DbUtils.releaseStatement(preparedStat);
        }
        return tables;
    }

    @Override
    public Format getFormat(Format format) {
        Statement stat = null;
        ResultSet ret = null;
        try {
            stat = conn.createStatement();
            ret = stat.executeQuery(String.format(TABLECOLUMNS, format.getName()));
            if (!ret.isClosed()) {
                Format fields = format.getChild(FIELDS);
                if (fields != null) {
                    while (ret.next()) {
                        String columnName = ret.getString(1);
                        String dataType = ret.getString(2);
                        String dataLength = ret.getString(3);
                        Format field = fields.addChild(columnName, Form.FIELD, toType(dataType));
                        field.setProperty(DATA_TYPE, new Value(dataType));
                        field.setProperty(DATA_LENGTH, new Value(dataLength));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.releaseResultSet(ret);
            DbUtils.releaseStatement(stat);
        }

        return format;
    }

    private Type toType(String dataType) {
        if (dataType.equalsIgnoreCase(CHAR) || dataType.startsWith(VARCHAR2)
            || dataType.startsWith(NVARCHAR2) || dataType.equalsIgnoreCase(CLOB)) {
            return Type.STRING;
        } else if (dataType.startsWith(NUMBER)) {
            return Type.INTEGER;
        } else if (dataType.equalsIgnoreCase(DATE)) {
            return Type.DATE;
        } else if (dataType.startsWith(NUMBERIC)) {
            return Type.DOUBLE;
        } else if (dataType.equalsIgnoreCase(BLOB)) {
            return Type.BINARY;
        }
        return Type.NONE;
    }

    public void execute(String SQL) {
        Statement stat = null;
        try {
            stat = conn.createStatement();
            stat.execute(SQL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.releaseStatement(stat);
        }
    }

    public List<Element> executeQuery(String SQL, ElementFromDbBuilder elementBuilder, Format output) {
        Statement stat = null;
        ResultSet ret = null;
        try {
            stat = conn.createStatement();
            ret = stat.executeQuery(SQL);
            return elementBuilder.buildElement(output, ret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.releaseResultSet(ret);
            DbUtils.releaseStatement(stat);
        }
    }
}
