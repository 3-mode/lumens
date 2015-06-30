/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.rapsync.api.LogMiner;
import com.lumens.connector.Operation;
import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import static com.lumens.connector.database.DBConstants.ACTION;
import static com.lumens.connector.database.DBConstants.DATA_LENGTH;
import static com.lumens.connector.database.DBConstants.DATA_TYPE;
import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import static com.lumens.connector.database.DBConstants.WHERE;
import com.lumens.connector.database.DBUtils;
import com.lumens.connector.rapsync.api.LogMinerFactory;
import com.lumens.connector.rapsync.api.Config;
import com.lumens.connector.rapsync.api.ConfigFactory;
import com.lumens.connector.rapsync.impl.DatabaseClient;
import com.lumens.logsys.SysLogFactory;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapSyncConnector implements Connector, RapSyncConstants {

    private final Logger log = SysLogFactory.getLogger(RapSyncConnector.class);

    private List<String> enforceFormatList = new ArrayList();
    protected Map<String, Format> inFormat;
    protected Map<String, Format> outFormat;
    private Config config = null;
    private String dbDriver = null;
    private String dbUrl = null;
    private String dbUserName = null;
    private String dbPassword = null;
    private String sessionAlter = null;

    private LogMiner miner = null;
    private DatabaseClient dbClient = null;

    boolean isOpen = false;

    public RapSyncConnector() {
        config = ConfigFactory.createDefaultConfig();
        buildFormatEnforcementList();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void open() {
        try {
            dbClient = new DatabaseClient(dbDriver, dbUrl, dbUserName, dbPassword);
            PrepareDatabase();
            miner = LogMinerFactory.createLogMiner(dbClient, config);

            isOpen = true;
        } catch (Exception ex) {
            log.error(String.format("DB connection driver: %s, url: %s, username:%s, password: %s", dbDriver, dbUrl, dbUserName, dbPassword));
            log.error("Fail to open RapSync connector. Error message:" + ex.getMessage());
            throw new RuntimeException("Fail to open RapSync connector. Error message:" + ex.getMessage());
        }
    }

    private void PrepareDatabase() {
        if (sessionAlter != null && !sessionAlter.isEmpty() && dbClient != null) {
            try {
                String[] alterList = sessionAlter.split(";");
                for (String alter : alterList) {
                    alter = alter.trim();
                    if (!alter.isEmpty()) {
                        dbClient.execute(alter.trim());
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void start() {
        miner.buildDictionary();
        miner.build(); // start build directory if specifying FILE type
        miner.start();
    }

    @Override
    public void stop() {
        miner.end();
    }

    @Override
    public void close() {
        if (miner != null) {
            miner = null;
        }
        if (dbClient != null) {
            dbClient.release();
            dbClient = null;
        }
        isOpen = false;
        inFormat = null;
        outFormat = null;
    }

    @Override
    public Operation getOperation() {
        return new RapSyncOperation(miner);
    }

    // get redo log fields from db
    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        if (inFormat != null && Direction.IN == direction) {
            return inFormat;
        } else if (outFormat != null && Direction.OUT == direction) {
            return outFormat;
        }

        Map<String, Format> formatList = new HashMap();
        Format rootFmt = new DataFormat(FORMAT_NAME, Form.STRUCT);

        if (direction == Direction.OUT) {
            outFormat = formatList;
        } else if (direction == Direction.IN) {
            Format SQLParams = rootFmt.addChild(SQLPARAMS, Format.Form.STRUCT);
            SQLParams.addChild(ACTION, Format.Form.FIELD, Type.STRING);
            SQLParams.addChild(WHERE, Format.Form.FIELD, Type.STRING);
            SQLParams.addChild(TABLE_LIST, Format.Form.FIELD, Type.STRING);
            inFormat = formatList;
        }
        formatList.put(FORMAT_NAME, rootFmt);
        getFormat(rootFmt, null, direction);

        return formatList;
    }

    // TODO: read from file
    public void buildFormatEnforcementList() {
        enforceFormatList.clear();
        for (String item : DISPLAY_FIELDS.split(",")) {
            enforceFormatList.add(item.trim());
        }
    }

    public boolean checkFormatEnforcement(String format) {
        return enforceFormatList.contains(format.trim());
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        ResultSet columns = null;
        try {
            columns = dbClient.executeGetResult(SQL_QUERY_COLUMNS);
            if (!columns.next()) {
                log.error("Insuffucient priviledga to access table 'all_tab_columns'");
                throw new RuntimeException("Insuffucient privilege to access table 'all_tab_columns' ");
            }
            do {
                String columnName = columns.getString(1);
                String dataType = columns.getString(2);
                if (dataType.equalsIgnoreCase("raw") || !checkFormatEnforcement(columnName)) {
                    continue;   // TODO: support raw datatype 
                }
                String dataLength = columns.getString(3);
                Format field = format.addChild(columnName, Format.Form.FIELD, toType(dataType));
                field.setProperty(DATA_TYPE, new Value(dataType));
                field.setProperty(DATA_LENGTH, new Value(dataLength));
            } while (columns.next());
        } catch (SQLException | RuntimeException ex) {
            log.error("Fail to get format. Error message:" + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            DBUtils.releaseResultSet(columns);
            dbClient.releaseStatement();
        }

        return format;
    }

    @Override
    public void setPropertyList(Map<String, Value> parameters) {

        // setup connection
        if (parameters.containsKey(DATABASE_DRIVER)) {
            dbDriver = parameters.get(DATABASE_DRIVER).getString();
        }
        if (parameters.containsKey(DATABASE_CONNECTION_URL)) {
            dbUrl = parameters.get(DATABASE_CONNECTION_URL).getString();
        }
        if (parameters.containsKey(DATABASE_CONNECTION_USERNAME)) {
            dbUserName = parameters.get(DATABASE_CONNECTION_USERNAME).getString();
        }
        if (parameters.containsKey(DATABASE_CONNECTION_PASSWORD)) {
            dbPassword = parameters.get(DATABASE_CONNECTION_PASSWORD).getString();
        }
        if (parameters.containsKey(SESSION_ALTER)) {
            sessionAlter = parameters.get(SESSION_ALTER).getString();
        }

        // setup config
        if (parameters.containsKey(PAGE_SIZE)) {
            config.setPageSize(parameters.get(PAGE_SIZE).getInt());
        }

        // Warning: those parameters should only specify one time or will introduce bugs
        if (parameters.containsKey(LOG_TYPE) && parameters.get(LOG_TYPE).getString().equalsIgnoreCase(REDOLOG_TYPE_OFFLINE)) {
            config.setBuildType(LogMiner.LOG_TYPE.OFFLINE);
        } else {
            config.setBuildType(LogMiner.LOG_TYPE.ONLINE);  // default online for incremental sync
        }
        //TODO: transit to online while offline redo log files finish

        if (parameters.containsKey(DICT_TYPE_STORE_IN_REDO_LOG)) {
            config.setDictType(LogMiner.DICT_TYPE.STORE_IN_REDO_LOG);
        } else if (parameters.containsKey(DICT_TYPE_STORE_IN_FILE)) {
            config.setDictType(LogMiner.DICT_TYPE.STORE_IN_FILE);
        } else {
            config.setDictType(LogMiner.DICT_TYPE.ONLINE);  // default online for performance
        }

        if (parameters.containsKey(COMMITED_DATA_ONLY)) {
            config.setCommittedDataOnly(parameters.get(COMMITED_DATA_ONLY).getBoolean());
        }
        if (parameters.containsKey(NO_ROWID)) {
            config.setNoRowid(parameters.get(NO_ROWID).getBoolean());
        }
        if (parameters.containsKey(START_SCN)) {
            config.setStartSCN(parameters.get(START_SCN).getString());
        }
        if (parameters.containsKey(END_SCN)) {
            config.setStartSCN(parameters.get(END_SCN).getString());
        }
    }

    private Type toType(String dataType) {
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
}
