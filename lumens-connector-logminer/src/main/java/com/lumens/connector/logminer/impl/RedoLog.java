/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.logminer.LogMinerClient;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RedoLog implements Constants{

    DatabaseClient dbClient;

    public RedoLog(DatabaseClient dbClient) {
        this.dbClient = dbClient;
    }

    List<String> getOnlineFileList() throws Exception{
        List<String> list = new ArrayList();
        ResultSet resultSet = dbClient.executeGetResult(SQL_QUERY_LOGFILE);
        while (resultSet.next()) {
            list.add(resultSet.getString(1));
        }
        return list;
    }

    List<String> getOfflineFileList() throws Exception{
        List<String> list = new ArrayList();
        ResultSet resultSet = dbClient.executeGetResult(SQL_QUERY_ARCHIVED_LOG);
        while (resultSet.next()) {
            list.add(resultSet.getString(1));
        }
        return list;
    }
}