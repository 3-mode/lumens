/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.dao;

import com.lumens.sysdb.entity.Configuration;
import java.util.List;

/**
 *
 * @author author shaofeng.wang@outlook.com
 */
public class ConfigurationDAO extends BaseDAO {

    public long create(final Configuration config) {
        simplePrepareStatTransactionExecute(sqlManager.getSQL("ConfigDAO/CreateConfig"), config.id, config.configName, config.description, config.configuration);
        return config.id;
    }

    public long update(final Configuration config) {
        simplePrepareStatTransactionExecute(sqlManager.getSQL("ConfigDAO/UpdateConfig"), config.id, config.configName, config.description, config.configuration);
        return config.id;
    }

    public long delete(final long configID) {
        simplePrepareStatTransactionExecute(sqlManager.getSQL("ConfigDAO/DeleteConfig"), configID);
        return configID;
    }

    public Configuration getConfiguration(final long configID) {
        List<Configuration> pList = simpleQuery(sqlManager.getSQL("ConfigDAO/FindConfig", configID), Configuration.class);
        return pList.size() > 0 ? pList.get(0) : null;
    }

    public List<Configuration> getAllConfig() {
        return simpleQuery(sqlManager.getSQL("ConfigDAO/AllConfig"), Configuration.class);
    }
}
