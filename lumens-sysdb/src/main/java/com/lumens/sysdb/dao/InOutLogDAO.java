/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.dao;

import com.lumens.sysdb.entity.InOutLogItem;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class InOutLogDAO extends BaseDAO {
    public void create(final InOutLogItem inoutLogItem) {
        this.simplePrepareStatTransactionExecute(sqlManager.getSQL("InOutLogDAO/PutLog"), inoutLogItem.logID,
                                                 inoutLogItem.componentID, inoutLogItem.componentName,
                                                 inoutLogItem.projectID, inoutLogItem.projectName, inoutLogItem.direction,
                                                 inoutLogItem.targetName, inoutLogItem.data, inoutLogItem.lastModifTime);
    }

    public List<InOutLogItem> getLogList(long projectID, long componentID) {
        return simpleQuery(sqlManager.getSQL("InOutLogDAO/GetLogByComponent", projectID, componentID), InOutLogItem.class);
    }

    public void deleteAllLog(long projectID) {
        this.simpleTransactionExecute(sqlManager.getSQL("InOutLogDAO/ClearLog", projectID));
    }
}
