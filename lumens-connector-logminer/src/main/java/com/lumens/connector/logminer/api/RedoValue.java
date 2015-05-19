/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.api;

import java.util.Date;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RedoValue {
    public int SCN;
    public int START_SCN;
    public int COMMIT_SCN;
    public Date TIMESTAMP;
    public Date START_TIMESTAMP;
    public Date COMMIT_TIMESTAMP;
    public String SEG_OWNER;
    public String SEG_NAME;
    public String TABLE_NAME;
    public int SEG_TYPE;
    public String TABLE_SPACE;
    public String ROW_ID;
    public String USERNAME;
    public String OS_USERNAME;
    public String MACHINE_NAME;
    public String SQL_REDO;
    public String SQL_UNDO;
    public int STATUS;
    public int REDO_VALUE; 
    public int UNDO_VALUE;    
    public String OPERATION;    
}
