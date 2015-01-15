/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.Direction;
import com.lumens.connector.database.Client;
import com.lumens.connector.database.DBConstants;
import com.lumens.connector.database.DbUtils;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public abstract class AbstractClient implements Client, DBConstants {

    protected URLClassLoader driverLoader;
    protected Driver driver;
    protected Connection conn;
    protected DBElementBuilder elementBuilder;
    protected DBConnector connector;

    public AbstractClient(DBConnector connector, String driverClass) {
        try {
            this.connector = connector;
            driver = (Driver) DbUtils.getInstance(connector.getOjdbcURL(), driverClass);
            this.elementBuilder = new DBElementBuilder();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void open() {
        conn = DbUtils.getConnection(driver, connector.getConnURL(), connector.getUser(), connector.getPassword());
    }

    @Override
    public void close() {
        DbUtils.releaseConnection(conn);
    }

    @Override
    public int getPageSize() {
        return connector.getPageSize();
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
            ret = stat.executeQuery(getTableNameQuerySQL());
            if (!ret.isClosed()) {
                while (ret.next()) {
                    Format table = createTableFormat(ret);
                    tables.put(table.getName(), table);
                    if (direction == Direction.IN) {
                        Format SQLParams = table.addChild(SQLPARAMS, Format.Form.STRUCT);
                        SQLParams.addChild(ACTION, Format.Form.FIELD, Type.STRING);
                        SQLParams.addChild(WHERE, Format.Form.FIELD, Type.STRING);
                        SQLParams.addChild(ORDERBY, Format.Form.FIELD, Type.STRING);
                        SQLParams.addChild(GROUPBY, Format.Form.FIELD, Type.STRING);
                    }
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
            ret = stat.executeQuery(String.format(getTableColumnQuerySQL(), format.getName()));
            if (!ret.isClosed()) {
                Format fields = format;
                if (fields != null) {
                    while (ret.next()) {
                        String columnName = ret.getString(1);
                        String dataType = ret.getString(2);
                        String dataLength = ret.getString(3);
                        Format field = fields.addChild(columnName, Format.Form.FIELD, toType(dataType));
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

    @Override
    public void execute(String SQL) {
        Statement stat = null;
        try {
            stat = conn.createStatement();
            stat.execute(SQL);
            conn.commit();
        } catch (Exception e) {
            DbUtils.rollback(conn);
            throw new RuntimeException(SQL, e);
        } finally {
            DbUtils.releaseStatement(stat);
        }
    }

    @Override
    public List<Element> executeQuery(String SQL, Format output) {
        Statement stat = null;
        ResultSet ret = null;
        try {
            stat = conn.createStatement();
            ret = stat.executeQuery(SQL);
            return elementBuilder.buildElement(output, ret);
        } catch (Exception e) {
            throw new RuntimeException(SQL, e);
        } finally {
            DbUtils.releaseResultSet(ret);
            DbUtils.releaseStatement(stat);
        }
    }

    protected abstract Type toType(String dataType);

    protected abstract String getTableNameQuerySQL();

    protected abstract String getTableColumnQuerySQL();

    protected abstract Format createTableFormat(ResultSet ret) throws SQLException;
}
