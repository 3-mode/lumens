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
public class RedoLog implements Constants {

    DatabaseClient dbClient;

    public RedoLog(DatabaseClient dbClient) {
        this.dbClient = dbClient;
    }

    public List<String> getOnlineFileList() throws Exception {
        List<String> list = new ArrayList();
        ResultSet resultSet = dbClient.executeGetResult(SQL_QUERY_LOGFILE);
        while (resultSet.next()) {
            list.add(resultSet.getString(1));
        }
        return list;
    }

    public List<String> getOfflineFileList() throws Exception {
        List<String> list = new ArrayList();
        ResultSet resultSet = dbClient.executeGetResult(SQL_QUERY_ARCHIVED_LOG);
        while (resultSet.next()) {
            list.add(resultSet.getString(1));
        }
        return list;
    }

    public String buildLogMinerStringFromList(List<String> list, boolean isFirstLog) {
        if (list.size() == 0) {
            throw new RuntimeException("input redo log list should not be empty");
        }

        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append(" BEGIN");
        boolean isFirst = isFirstLog;
        for (String item : list) {
            if (isFirst) {
                sbSQL.append(" dbms_logmnr.add_logfile(logfilename=>'").append(item).append("', options=>dbms_logmnr.NEW);");
                isFirst = false;
            } else {
                sbSQL.append(" dbms_logmnr.add_logfile(logfilename=>'").append(item).append("', options=>dbms_logmnr.ADDFILE);");
            }

        }
        sbSQL.append(" END;");

        return sbSQL.toString();
    }
}
