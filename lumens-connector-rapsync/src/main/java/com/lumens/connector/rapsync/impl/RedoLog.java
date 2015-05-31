/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

import com.lumens.logsys.SysLogFactory;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RedoLog implements Constants {

    private final Logger log = SysLogFactory.getLogger(RedoLog.class);
    DatabaseClient dbClient;
    private Boolean isSupplementalLog = null;
    private Boolean isArchivedLogMode = null;

    public RedoLog(DatabaseClient dbClient) {
        this.dbClient = dbClient;
    }

    public List<String> getOnlineFileList() throws Exception {
        List<String> list = new ArrayList();

        try (ResultSet resultSet = dbClient.executeGetResult(SQL_QUERY_LOGFILE)) {
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            dbClient.releaseStatement();
        }
        return list;
    }

    public List<String> getOfflineFileList() throws Exception {
        List<String> list = new ArrayList();
        try (ResultSet resultSet = dbClient.executeGetResult(SQL_QUERY_ARCHIVED_LOG)) {
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            dbClient.releaseStatement();
        }
        return list;
    }

    public String buildLogMinerStringFromList(List<String> list, boolean isFirstLog) {
        if (list.isEmpty()) {
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

        if (log.isDebugEnabled()) {
            log.debug(String.format("Redolog build list: %s", sbSQL.toString()));
        }
        return sbSQL.toString();
    }

    public boolean isArchivedLogModeEnabled() {
        if (isArchivedLogMode == null) {
            try (ResultSet result = dbClient.executeGetResult(SQL_CHECK_LOG_MODE)) {
                if (result.next()) {
                    isArchivedLogMode = result.getString(1).equalsIgnoreCase("archivelog");
                }
            } catch (Exception ex) {
                log.error("Fail to get Oracle supplemental log. Error message: " + ex.getMessage());
            } finally {
                dbClient.releaseStatement();
            }
        }

        return isArchivedLogMode;
    }

    public boolean isSupplementalLogEnabled() {
        if (isSupplementalLog == null) {
            try (ResultSet result = dbClient.executeGetResult(SQL_CHECK_SUPPLEMENTAL_LOG)) {
                if (result.next()) {
                    String res = result.getString(1);
                    isSupplementalLog = res.equalsIgnoreCase("yes") || res.equalsIgnoreCase("implicit");
                }
            } catch (Exception ex) {
                log.error("Fail to get Oracle supplemental log. Error message: " + ex.getMessage());
            } finally {
                dbClient.releaseStatement();
            }
        }
        return isSupplementalLog;
    }

    public boolean enableSupplementalLog() {
        boolean bSuccess = false;
        try {
            dbClient.execute(SQL_ENABLE_SUPPLEMENTAL_LOG);
            bSuccess = true;
            isSupplementalLog = true;
        } catch (Exception ex) {
            log.error("Fail to enable Oracle supplemental log. Error message: " + ex.getMessage());
        }

        return bSuccess;
    }

}
