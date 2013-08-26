/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.database.client.oracle.OracleClient;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseConnector implements Connector {
    public static final String OJDBC = "OJDBC";
    public static final String CONNECTION_URL = "ConnectionURL";
    public static final String USER = "User";
    public static final String PASSWORD = "Password";
    public static final String FULL_LOAD = "FullLoad";
    private Client dbClient;
    private Map<String, Format> tables;
    private String ojdbcURL;
    private String connURL;
    private String user;
    private String password;
    private boolean fullLoad;

    @Override
    public void open() {
        // TODO only create oracle client now, select different db client later
        dbClient = new OracleClient(ojdbcURL, connURL, user, password);
        dbClient.open();
        tables = dbClient.getFormatList(fullLoad);
    }

    @Override
    public void close() {
        if (dbClient != null) {
            dbClient.close();
        }
        tables = null;
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        return tables;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        return dbClient.getFormat(format);
    }

    @Override
    public Operation getOperation() {
        return new DatabaseOperation(dbClient);
    }

    @Override
    public void setPropertyList(Map<String, Value> propertyList) {
        if (propertyList.containsKey(OJDBC)) {
            ojdbcURL = propertyList.get(OJDBC).getString();
        }
        if (propertyList.containsKey(CONNECTION_URL)) {
            connURL = propertyList.get(CONNECTION_URL).getString();
        }
        if (propertyList.containsKey(USER)) {
            user = propertyList.get(USER).getString();
        }
        if (propertyList.containsKey(PASSWORD)) {
            password = propertyList.get(PASSWORD).getString();
        }
        if (propertyList.containsKey(FULL_LOAD)) {
            fullLoad = propertyList.get(FULL_LOAD).getBoolean();
        }
    }
}
