/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.api;

import com.lumens.connector.rapsync.api.LogMiner.LOG_TYPE;
import com.lumens.connector.rapsync.api.LogMiner.DICT_TYPE;
import com.lumens.logsys.SysLogFactory;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class Config {

    private final Logger log = SysLogFactory.getLogger(Config.class);
    private DICT_TYPE dict_type = DICT_TYPE.ONLINE;
    private LOG_TYPE build_type = LOG_TYPE.ONLINE;
    private boolean isCommittedDataOnly = true;
    private boolean isNoRowid = true;
    private String startSCN = "0";
    private String endSCN = null;
    private boolean isSupplementalLogMode = false;
    private boolean isNoSQLDelimiter = true;
    private boolean isContinuousMine = false;
    private boolean isSkipCorruption = true;
    private int pageSize = 1000;
    private boolean isDDLTracking = false;

    public void setPageSize(int size) {
        this.pageSize = size;
    }

    public int getPageSize() {
        return pageSize;
    }

    public DICT_TYPE getDictType() {
        return dict_type;
    }

    public LOG_TYPE getLogType() {
        return build_type;
    }

    public void setEnabledSupplementalLog() {
        isSupplementalLogMode = true;
    }

    public boolean isSupplementalLogEnabled() {
        return isSupplementalLogMode;
    }

    public void setDictType(DICT_TYPE dictType) {
        this.dict_type = dictType;
    }

    public void setBuildType(LOG_TYPE buildType) {
        this.build_type = buildType;
    }

    public void setNoRowid(boolean isNoRowid) {
        this.isNoRowid = isNoRowid;
    }

    public void setNoSQLDelimiter(boolean isNoDelimiter) {
        this.isNoSQLDelimiter = isNoDelimiter;
    }

    public void setContinuousMine(boolean isContinuousMine) {
        this.isContinuousMine = isContinuousMine;
    }

    public void setCommittedDataOnly(boolean isCommittedOnly) {
        isCommittedDataOnly = isCommittedOnly;
    }

    public void setStartSCN(String scn) {
        try {
            Double.parseDouble(scn);
            this.startSCN = scn;
        } catch (Exception ex) {
            log.warn(String.format("Not a valid SCN: %s, ignore parameter", scn));
        }
    }

    public void setEndSCN(String scn) {
        try {
            Double.parseDouble(scn);
            this.endSCN = scn;
        } catch (Exception ex) {
            log.warn(String.format("Not a valid SCN: %s, ignore parameter", scn));
        }
    }

    public String buildParameters() {
        isDDLTracking = dict_type != DICT_TYPE.ONLINE;

        StringBuilder parameter = new StringBuilder();
        if (startSCN != null) {
            parameter.append(String.format("STARTSCN =>%s", startSCN));
        }
        if (endSCN != null) {
            parameter.append(String.format(",ENDSCN =>%s", endSCN));
        }

        if (dict_type == DICT_TYPE.STORE_IN_FILE) {
            parameter.append(", DICTFILENAME => '%s\\%s'");
        }

        StringBuilder option = new StringBuilder();
        if (dict_type == DICT_TYPE.ONLINE) {
            option.append("DBMS_LOGMNR.DICT_FROM_ONLINE_CATALOG");
        } else if (dict_type == DICT_TYPE.STORE_IN_REDO_LOG) {
            option.append("DBMS_LOGMNR.DICT_FROM_REDO_LOGS");
        }

        // Optional 
        if (isContinuousMine) {
            if (option.length() > 0) {
                option.append(" + ");
            }
            option.append("DBMS_LOGMNR.CONTINUOUS_MINE");
        }

        if (isNoSQLDelimiter) {
            if (option.length() > 0) {
                option.append(" + ");
            }
            option.append("DBMS_LOGMNR.NO_SQL_DELIMITER");
        }

        if (isCommittedDataOnly) {
            if (option.length() > 0) {
                option.append(" + ");
            }
            option.append("DBMS_LOGMNR.COMMITTED_DATA_ONLY");
        }
        if (isNoRowid) {
            if (option.length() > 0) {
                option.append(" + ");
            }
            option.append("DBMS_LOGMNR.NO_ROWID_IN_STMT");
        }
        if (isSkipCorruption) {
            if (option.length() > 0) {
                option.append(" + ");
            }
            option.append("DBMS_LOGMNR.SKIP_CORRUPTION");
        }
        if (startSCN != null && endSCN != null) {
            if (option.length() > 0) {
                option.append(" + ");
            }
            option.append("DBMS_LOGMNR.CONTINUOUS_MINE");
        }
        if (isDDLTracking) {
            if (option.length() > 0) {
                option.append(" + ");
            }
            option.append("DBMS_LOGMNR.DDL_DICT_TRACKING");
        }
        
        if (option.length() > 0) {
            parameter.append(", OPTIONS => ").append(option);
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("Config parameters: %s", parameter.toString()));
        }
        return parameter.toString();
    }
}
