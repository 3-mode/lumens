/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

import com.lumens.connector.logminer.api.LogMiner;
import com.lumens.connector.Operation;
import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import static com.lumens.connector.database.DBConstants.DATA_LENGTH;
import static com.lumens.connector.database.DBConstants.DATA_TYPE;
import com.lumens.connector.logminer.api.LogMinerFactory;
import com.lumens.connector.logminer.api.Config;
import com.lumens.connector.logminer.impl.DatabaseClient;
import com.lumens.connector.logminer.impl.LogMinerImpl;
import com.lumens.logsys.LogSysFactory;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.sql.ResultSet;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerConnector implements Connector, LogMinerConstants {

    private final Logger log = LogSysFactory.getLogger(LogMinerImpl.class);
    private Config config = null;
    private String dbDriver = null;
    private String dbUrl = null;
    private String dbUserName = null;
    private String dbPassword = null;

    private LogMiner miner = null;
    private DatabaseClient dbClient = null;

    boolean isOpen = false;

    public LogMinerConnector() {
        config = new Config();
        config.setBuildType(LogMiner.BUILD_TYPE.OFFLINE);
        config.setDictType(LogMiner.DICT_TYPE.STORE_IN_REDO_LOG);
        config.setCommittedDataOnly(true);
        config.setNoRowid(true);
        config.setStartSCN("0");
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
    public void close() {
        miner.end();
    }

    @Override
    public Operation getOperation() {
        return new LogMinerOperation(miner);
    }

    // get redo log fields from db
    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        Map<String, Format> formatList = new HashMap();
        Format rootFmt = new DataFormat(FORMAT_NAME, Form.STRUCT);
        formatList.put(FORMAT_NAME, rootFmt);
        getFormat(rootFmt, null, direction);

        return formatList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        if (direction == Direction.IN) {
            try {
                ResultSet columns = dbClient.executeGetResult(SQL_QUERY_COLUMNS);
                while (columns.next()) {
                    String columnName = columns.getString(1);
                    String dataType = columns.getString(2);
                    String dataLength = columns.getString(3);
                    Format field = format.addChild(columnName, Format.Form.FIELD, toType(dataType));
                    field.setProperty(DATA_TYPE, new Value(dataType));
                    field.setProperty(DATA_LENGTH, new Value(dataLength));
                }
            } catch (Exception ex) {
                log.error("Fail to get format. Error message:" + ex.getMessage());
                throw new RuntimeException(ex);
            }
        }else{
            
        }
        
        return format;
    }

    @Override
    public void start() {
        miner.build(); // start build directory if specifying FILE type
        miner.start();
    }

    @Override
    public void stop() {
    }

    @Override
    public void setPropertyList(Map<String, Value> parameters) {

        // setup connection
        if (parameters.containsKey(DATABASE_DRIVER)) {
            dbDriver = parameters.get(DATABASE_DRIVER).getString();
        }
        if (parameters.containsKey(DATABASE_SOURCE_URL)) {
            dbUrl = parameters.get(DATABASE_SOURCE_URL).getString();
        }
        if (parameters.containsKey(DATABASE_SOURCE_USERNAME)) {
            dbUserName = parameters.get(DATABASE_SOURCE_USERNAME).getString();
        }
        if (parameters.containsKey(DATABASE_SOURCE_PASSWORD)) {
            dbPassword = parameters.get(DATABASE_SOURCE_PASSWORD).getString();
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
