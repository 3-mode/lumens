/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.database.DatabaseConstants;
import static com.lumens.connector.database.DatabaseConstants.FULL_LOAD;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author testing
 */
public class SqlServerConnector implements Connector, DatabaseConstants{
 
    private SqlServerClient dbClient;
    private Map<String, Format> tables;
    private String ojdbcURL;
    private String connURL;
    private String user;
    private String password;
    private boolean fullLoad = true;
    private String sessionAlter;
    private String databaseName;
    private boolean isOpen;

    @Override
    public void open() {
        // TODO only create oracle client now, select different db client later
        if (dbClient == null) {
            dbClient = new SqlServerClient(ojdbcURL, connURL, user, password, sessionAlter);
            dbClient.open();
            isOpen = true;
        }
    }

    @Override
    public void close() {
        if (dbClient != null) {
            dbClient.close();
        }
        isOpen = false;
        dbClient = null;
        tables = null;

    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        if (tables == null)
            tables = dbClient.getFormatList(direction, fullLoad);
        return tables;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        return dbClient.getFormat(format);
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
        if (propertyList.containsKey(SESSION_ALTER)) {
            sessionAlter = propertyList.get(SESSION_ALTER).getString();
        }
        if (propertyList.containsKey(DATABASE_NAME)) {
            databaseName = propertyList.get(DATABASE_NAME).getString();
        }        
    }

    @Override
    public Operation getOperation() {
        return new SqlServerOperation(dbClient);
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }   
}
