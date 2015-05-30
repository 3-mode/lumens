/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

import java.sql.ResultSet;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class Dictionary implements Constants {

    private String SQL_CREATE_DIECTIONARY = null; // for example "BEGIN dbms_logmnr_d.build(dictionary_filename => 'dictionary.ora', dictionary_location =>'" + DATABASE_DICTIONARY_PATH + "'); END;";
    private DatabaseClient dbClient = null;

    public Dictionary(DatabaseClient dbClient) {
        this.dbClient = dbClient;
    }

    public void build() throws Exception {
        String path = getDictionaryPath();
        SQL_CREATE_DIECTIONARY = "BEGIN dbms_logmnr_d.build(dictionary_filename => '" + DICTIONARY_FILE + "', dictionary_location =>'" + path + "'); END;";
        dbClient.execute(SQL_CREATE_DIECTIONARY);
    }

    public String getDictionaryPath() throws Exception {
        try (ResultSet resultSet = dbClient.executeGetResult(SQL_QUERY_DIRECTORY_PATH)) {
            if (!resultSet.next()) {
                throw new RuntimeException("Dictionary path has not set.");
            }
            return resultSet.getString(1);
        } finally {
            dbClient.releaseStatement();
        }
    }
}
