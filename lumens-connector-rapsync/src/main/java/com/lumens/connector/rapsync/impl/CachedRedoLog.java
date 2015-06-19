/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

import com.lumens.connector.rapsync.api.LogMiner.LOG_TYPE;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class CachedRedoLog extends RedoLog {

    private List<String> logList = null;
    private int logCount = 0;
    private int index = 0;
    private int queryPageSize = 10;

    public CachedRedoLog(DatabaseClient dbClient) {
        super(dbClient);
    }

    public void setPageSize(int page){
        queryPageSize = page;
    }
    
    private class RedoLogQueryImpl implements RedoLogQuery {

        @Override
        public boolean hasNext() {
            if (logCount == 0) {
                logCount = getLogCount();
            }
            if (logList == null) {
                try {
                    logList = logType == LOG_TYPE.ONLINE ? getOnlineFileList() : getOfflineFileList();
                } catch (Exception ex) {
                    log.error("Fail to get redo log list. Error message:%s" + ex.getMessage());
                }
                return CachedRedoLog.this.index < CachedRedoLog.this.logCount;
            }

            return index < logCount;
        }

        @Override
        public List<String> next() {
            int last = index + queryPageSize;
            if (last > logCount) {
                last = logCount;
            }
            List<String> list = logList.subList(index, last);
            index += last;

            return list;
        }
    }

    RedoLogQuery getQuery() {
        return new RedoLogQueryImpl();
    }
}
