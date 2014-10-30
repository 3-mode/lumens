/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.database.DatabaseConstants;
import com.lumens.connector.database.ElementFromDbBuilder;
import com.lumens.connector.Direction;
import com.lumens.connector.database.DbUtils;
import com.lumens.connector.database.client.AbstractClient;
import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_DATA_LENGTH;
import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_DATA_TYPE;
import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_FIELDS;
import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_TABLECOLUMNS;
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

public class SqlServerClient extends AbstractClient implements DatabaseConstants{
    public SqlServerClient(String ojdbcURL, String connURL, String user, String password, String sessionAlter) {
        super(ojdbcURL, CONST_CNTR_SQLSERVER_CLASS, connURL, user, password, sessionAlter);
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
    public Map<String, Format> getFormatList(Direction direction, boolean fullLoad) {
        Map<String, Format> tables = new HashMap<>();
        Statement stat = null;
        PreparedStatement preparedStat = null;
        ResultSet ret = null;
        ResultSet preparedRet = null;
        try {
            stat = conn.createStatement();
            ret = stat.executeQuery(CONST_CNTR_SQLSERVER_TABLENAMES);
            if (!ret.isClosed()) {
                while (ret.next()) {
                    String tableName = ret.getString(1);
                    String description = ret.getString(2);
                    String type = ret.getString(3);
                    Format table = new DataFormat(tableName, Form.STRUCT);
                    tables.put(tableName, table);
                    table.addChild(CONST_CNTR_SQLSERVER_FIELDS, Form.STRUCT);
                    if (direction == Direction.IN) {
                        table.addChild(CONST_CNTR_SQLSERVER_OPERATION, Form.FIELD, Type.STRING);
                        table.addChild(CONST_CNTR_SQLSERVER_CLAUSE, Form.FIELD, Type.STRING);
                    }
                    table.setProperty(CONST_CNTR_SQLSERVER_DESCRIPTION, new Value(description));
                    table.setProperty(CONST_CNTR_SQLSERVER_TYPE, new Value(type));
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
            ret = stat.executeQuery(String.format(CONST_CNTR_SQLSERVER_TABLECOLUMNS, format.getName()));
            if (!ret.isClosed()) {
                Format fields = format.getChild(CONST_CNTR_SQLSERVER_FIELDS);
                if (fields != null && fields.getChildren() == null || fields.getChildren().size() == 0) {
                    while (ret.next()) {
                        String columnName = ret.getString(1);
                        String dataType = ret.getString(2);
                        String dataLength = ret.getString(3);
                        Format field = fields.addChild(columnName, Form.FIELD, toType(dataType));
                        field.setProperty(CONST_CNTR_SQLSERVER_DATA_TYPE, new Value(dataType));
                        field.setProperty(CONST_CNTR_SQLSERVER_DATA_LENGTH, new Value(dataLength));
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
        if (dataType.equalsIgnoreCase(CONST_CNTR_SQLSERVER_CHAR) || dataType.startsWith(CONST_CNTR_SQLSERVER_VARCHAR2)
            || dataType.startsWith(CONST_CNTR_SQLSERVER_NVARCHAR2) || dataType.equalsIgnoreCase(CONST_CNTR_SQLSERVER_CLOB)) {
            return Type.STRING;
        } else if (dataType.startsWith(CONST_CNTR_SQLSERVER_NUMBER)) {
            return Type.INTEGER;
        } else if (dataType.equalsIgnoreCase(CONST_CNTR_SQLSERVER_DATE)) {
            return Type.DATE;
        } else if (dataType.startsWith(CONST_CNTR_SQLSERVER_NUMBERIC)) {
            return Type.DOUBLE;
        } else if (dataType.equalsIgnoreCase(CONST_CNTR_SQLSERVER_BLOB)) {
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
