/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface LogMinerConstants {
    public String DATABASE_DRIVER = "databaseDriver";
    public String DATABASE_SOURCE_URL = "databaseSourceUrl";
    public String DATABASE_SOURCE_USERNAME = "databaseSourceUserName";
    public String DATABASE_SOURCE_PASSWORD = "databaseSourcePassword";
    
    public String BUILD_TYPE_ONLINE = "buildTypeOnline";
    public String BUILD_TYPE_OFFLINE = "buildTypeOffline";
    public String DICT_TYPE_ONLINE = "dictTypeOnline";
    public String DICT_TYPE_STORE_IN_REDO_LOG = "dictTypesToreInRedoLog";
    public String DICT_TYPE_STORE_IN_FILE = "dictTypeStoreInFile";
    
    public String COMMITED_DATA_ONLY = "committedDataOnly";    
    public String NO_ROWID = "noRowId";
    public String START_SCN = "startScn";
    public String END_SCN = "endScn";
    
}
