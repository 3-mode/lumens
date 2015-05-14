/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.dao;

import com.lumens.sysdb.entity.ElementExceptionLog;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ElementExceptionDAO extends BaseDAO {
    public void create(final ElementExceptionLog elemExcepLog) {
        this.simpleTransactionExecute(sqlManager.getSQL("ElementExceptionDAO/PutLog", elemExcepLog.logID,
                                                        elemExcepLog.jobID, elemExcepLog.excepMessage,
                                                        elemExcepLog.componentID, elemExcepLog.componentName,
                                                        elemExcepLog.projectID, elemExcepLog.projectName,
                                                        elemExcepLog.direction, elemExcepLog.targetName,
                                                        elemExcepLog.data, elemExcepLog.lastModifTime));
    }

    public List<ElementExceptionLog> getLogList(long jobID, long projectID) {
        return simpleQuery(sqlManager.getSQL("ElementExceptionDAO/GetLogByJobProject", jobID, projectID),
                           ElementExceptionLog.class);
    }

    public void deleteAllLog(long jobID) {
        this.simpleTransactionExecute(sqlManager.getSQL("ElementExceptionDAO/ClearLog", jobID));
    }
}
