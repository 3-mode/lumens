/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public interface DBConstants {
    public String OJDBC = "OJDBC";
    public String CONNECTION_URL = "ConnectionURL";
    public String USER = "User";
    public String PASSWORD = "Password";
    public String FULL_LOAD = "FullLoad";
    public String SESSION_ALTER = "SessionAlter";
    public String PAGE_SIZE = "PageSize";

    // Key field words
    public String SELECT = "SELECT";
    public String INSERT_ONLY = "INSERT_ONLY";
    public String UPDATE_ONLY = "UPDATE_ONLY";
    public String UPDATE_OR_INSERT = "UPDATE_OR_INSERT";
    // Constants node names
    public String SQLPARAMS = "SQLParams";
    public String ORDERBY = "orderby";
    public String GROUPBY = "groupby";
    public String WHERE = "where";
    public String ACTION = "action";

    // SQL load table metadata
    public String TYPE = "type";
    public String DESCRIPTION = "description";
    public String DATA_LENGTH = "data_length";
    public String DATA_TYPE = "data_type";
}