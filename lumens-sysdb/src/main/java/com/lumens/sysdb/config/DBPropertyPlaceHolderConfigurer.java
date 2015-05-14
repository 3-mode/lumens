/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.config;

import com.lumens.logsys.LogSysFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DBPropertyPlaceHolderConfigurer extends PropertyPlaceholderConfigurer {

    private final Logger log = LogSysFactory.getLogger(DBPropertyPlaceHolderConfigurer.class);

    @Override
    public void setLocation(Resource location) {
        String homeBase = System.getProperty("lumens.base");
        if (homeBase == null || homeBase.isEmpty()) {
            log.info(String.format("Path to load jdbc is \"%s\"", location.getFilename()));
            super.setLocation(location);
        } else {
            log.info(String.format("Path to load jdbc is \"%s\"", homeBase + "/conf/data-access.properties"));
            super.setLocation(new PathResource(homeBase + "/conf/data-access.properties"));
        }
    }
}
