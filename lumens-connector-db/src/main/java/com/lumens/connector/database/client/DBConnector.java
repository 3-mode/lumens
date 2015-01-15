/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.database.Client;
import com.lumens.connector.database.DBConstants;
import static com.lumens.connector.database.DBConstants.FULL_LOAD;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 * TODO connector only support oracle now and operation on one table one time
 *
 * @author shaofeng wang
 */
public abstract class DBConnector implements Connector, DBConstants {

    protected Map<String, Format> inTables;
    protected Map<String, Format> outTables;
    protected Client dbClient;
    protected String ojdbcURL;
    protected String connURL;
    protected String user;
    protected String password;
    protected boolean fullLoad = true;

    public String getOjdbcURL() {
        return ojdbcURL;
    }

    public boolean isFullLoad() {
        return fullLoad;
    }
    protected boolean isOpen;
    protected int pageSize = 1000;

    public String getConnURL() {
        return connURL;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void open() {
        // TODO only create oracle client now, select different db client later
        if (dbClient == null) {
            dbClient = createDBClient();
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
        inTables = null;
        outTables = null;
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        if (inTables == null && Direction.IN == direction)
            inTables = dbClient.getFormatList(direction, fullLoad);
        else if (outTables == null && Direction.OUT == direction)
            outTables = dbClient.getFormatList(direction, fullLoad);
        return Direction.IN == direction ? inTables : outTables;
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
        if (propertyList.containsKey(PAGE_SIZE)) {
            pageSize = propertyList.get(PAGE_SIZE).getInt();
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    protected abstract Client createDBClient();
}
