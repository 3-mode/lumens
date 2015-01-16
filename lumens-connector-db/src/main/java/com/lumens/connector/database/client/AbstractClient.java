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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shaofeng wang
 */
public abstract class AbstractClient implements Client, DBConstants {

    protected URLClassLoader driverLoader;
    protected Driver driver;
    protected Connection conn;
    protected Statement stat;
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
        try {
            stat = conn.createStatement();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        DbUtils.releaseStatement(stat);
        DbUtils.releaseConnection(conn);
    }

    @Override
    public int getPageSize() {
        return connector.getPageSize();
    }

    @Override
    public void commit() {
        DbUtils.commit(conn);
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction, boolean fullLoad) {
        Map<String, Format> tables = new HashMap<>();
        try (ResultSet ret = stat.executeQuery(getTableNameQuerySQL())) {

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
            return tables;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Format getFormat(Format format) {
        try (ResultSet ret = stat.executeQuery(String.format(getTableColumnQuerySQL(), format.getName()))) {
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
            return format;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(String SQL) {
        try {
            stat.execute(SQL);
        } catch (Exception e) {
            DbUtils.rollback(conn);
            throw new RuntimeException(SQL, e);
        }
    }

    @Override
    public List<Element> executeQuery(String SQL, Format output) {
        try (ResultSet ret = stat.executeQuery(SQL)) {
            return elementBuilder.buildElement(output, ret);
        } catch (Exception e) {
            throw new RuntimeException(SQL, e);
        }
    }

    protected abstract Type toType(String dataType);

    protected abstract String getTableNameQuerySQL();

    protected abstract String getTableColumnQuerySQL();

    protected abstract Format createTableFormat(ResultSet ret) throws SQLException;
}
