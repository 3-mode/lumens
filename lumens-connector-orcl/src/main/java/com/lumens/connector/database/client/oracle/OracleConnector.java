/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.database.DatabaseConstants;
import static com.lumens.connector.database.DatabaseConstants.FULL_LOAD;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 * TODO connector only support oracle now and operation on one table one time
 *
 * @author shaofeng wang
 */
public class OracleConnector implements Connector, DatabaseConstants {

    private OracleClient dbClient;
    private Map<String, Format> tables;
    private String ojdbcURL;
    private String connURL;
    private String user;
    private String password;
    private boolean fullLoad = true;
    private String sessionAlter;
    private boolean isOpen;

    @Override
    public void open() {
        // TODO only create oracle client now, select different db client later
        if (dbClient == null) {
            dbClient = new OracleClient(ojdbcURL, connURL, user, password, sessionAlter);
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
    }

    @Override
    public Operation getOperation() {
        return new OracleOperation(dbClient);
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
}
