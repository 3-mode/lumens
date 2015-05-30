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
import static com.lumens.connector.database.DBConstants.GROUPBY;
import static com.lumens.connector.database.DBConstants.ORDERBY;
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
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapSyncConnector implements Connector, RapSyncConstants {

    private final Logger log = SysLogFactory.getLogger(RapSyncConnector.class);

    protected Map<String, Format> inFormat;
    protected Map<String, Format> outFormat;
    private Config config = null;
    private String dbDriver = null;
    private String dbUrl = null;
    private String dbUserName = null;
    private String dbPassword = null;

    private LogMiner miner = null;
    private DatabaseClient dbClient = null;

    boolean isOpen = false;

    public RapSyncConnector() {
        config = ConfigFactory.createDefaultConfig();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void open() {
        try {
            dbClient = new DatabaseClient(dbDriver, dbUrl, dbUserName, dbPassword);
            miner = LogMinerFactory.createLogMiner(dbClient, config);

            isOpen = true;
        } catch (Exception ex) {
            log.error("Fail to open LogMiner connector. Error message:" + ex.getMessage());
            throw new RuntimeException("Fail to open LogMiner connector. Error message:" + ex.getMessage());
        }
    }

    @Override
    public void start() {
        if (inFormat != null) {
            miner.buildDictionary();
            miner.build(); // start build directory if specifying FILE type
            miner.start();
        }
    }

    @Override
    public void stop() {
        if (inFormat != null) {
            miner.end();
        }
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
        if (direction == Direction.IN) {
            Format SQLParams = rootFmt.addChild(SQLPARAMS, Format.Form.STRUCT);
            SQLParams.addChild(ACTION, Format.Form.FIELD, Type.STRING);
            SQLParams.addChild(WHERE, Format.Form.FIELD, Type.STRING);
            SQLParams.addChild(ORDERBY, Format.Form.FIELD, Type.STRING);
            SQLParams.addChild(GROUPBY, Format.Form.FIELD, Type.STRING);
            inFormat = formatList;
        } else if (direction == Direction.OUT) {
            Format SQLParams = rootFmt.addChild(SQLPARAMS, Format.Form.STRUCT);
            SQLParams.addChild(ACTION, Format.Form.FIELD, Type.STRING);
            outFormat = formatList;
        }
        formatList.put(FORMAT_NAME, rootFmt);
        getFormat(rootFmt, null, direction);

        return formatList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        if (direction == Direction.IN) {
            ResultSet columns = null;
            try {
                columns = dbClient.executeGetResult(SQL_QUERY_COLUMNS);
                if (!columns.next()) {
                    log.error("Insuffucient priviledga to access table 'user_tab_columns'");
                    throw new RuntimeException("Insuffucient privilege to access table 'user_tab_columns' ");
                }
                do {
                    String columnName = columns.getString(1);
                    String dataType = columns.getString(2);
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
        } else if (direction == Direction.OUT) {
            Format scnField = format.addChild(COLUMN_SCN, Format.Form.FIELD, Type.STRING);
            scnField.setProperty(DATA_TYPE, new Value(NUMBER));
            scnField.setProperty(DATA_LENGTH, new Value(COLUMN_SCN_LENGTH));

            Format redoField = format.addChild(COLUMN_SCN, Format.Form.FIELD, Type.STRING);
            redoField.setProperty(DATA_TYPE, new Value(NVARCHAR2));
            redoField.setProperty(DATA_LENGTH, new Value(COLUMN_REDO_LENGTH));
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

        // setup config
        // Warning: those parameters should only specify one time or will introduce bugs
        if (parameters.containsKey(BUILD_TYPE_ONLINE)) {
            config.setBuildType(LogMiner.BUILD_TYPE.ONLINE);
        } else if (parameters.containsKey(BUILD_TYPE_OFFLINE)) {
            config.setBuildType(LogMiner.BUILD_TYPE.OFFLINE);
        }

        if (parameters.containsKey(DICT_TYPE_ONLINE)) {
            config.setDictType(LogMiner.DICT_TYPE.ONLINE);
        } else if (parameters.containsKey(DICT_TYPE_STORE_IN_REDO_LOG)) {
            config.setDictType(LogMiner.DICT_TYPE.STORE_IN_REDO_LOG);
        } else if (parameters.containsKey(DICT_TYPE_STORE_IN_FILE)) {
            config.setDictType(LogMiner.DICT_TYPE.STORE_IN_FILE);
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
