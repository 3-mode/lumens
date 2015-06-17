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
    private int size = 0;
    private int index = 0;

    public CachedRedoLog(DatabaseClient dbClient) {
        super(dbClient);
    }

    private class RedoLogQueryImpl implements RedoLogQuery {

        @Override
        public boolean hasNext() {
            if (size == 0) {
                size = CachedRedoLog.this.getLogCount();
            }
            if (logList == null) {
                try {
                    logList = CachedRedoLog.this.logType == LOG_TYPE.ONLINE ? CachedRedoLog.this.getOnlineFileList() : CachedRedoLog.this.getOfflineFileList();
                } catch (Exception ex) {
                    log.error("Fail to get redo log list. Error message:%s" + ex.getMessage());
                }
                return CachedRedoLog.this.index < CachedRedoLog.this.size;
            }

            return index < size;
        }

        @Override
        public List<String> next() {
            int last = index + 10;
            if (last > size) {
                last = size;
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
