/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.logminer.LogMinerClient;
import java.sql.ResultSet;
import java.io.File;

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

    public boolean createDictionary() throws Exception {
        String path = getDictionaryPath();
        SQL_CREATE_DIECTIONARY = "BEGIN dbms_logmnr_d.build(dictionary_filename => '" + DICTIONARY_FILE +"', dictionary_location =>'" + path + "'); END;";
        dbClient.execute(SQL_CREATE_DIECTIONARY);

        return new File(path + "/" + DICTIONARY_FILE).exists();
    }

    public String getDictionaryPath() throws Exception {
        ResultSet resultSet = dbClient.executeGetResult(SQL_QUERY_DIRECTORY_PATH);
        if (!resultSet.next()) {
            throw new RuntimeException("Dictionary path has not set.");
        }
        return resultSet.getString(1);
    }
}