/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.entity;

import com.lumens.sysdb.Column;
import com.lumens.sysdb.Table;


/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Table(name = "LUNS_CONFIGURATION")
public class Configuration {
    @Column(name = "ID")
    public Long id;
    @Column(name = "CONFIG_NAME")
    public String configName;
    @Column(name = "DESCRIPTION")
    public String description;
    @Column(name = "CONFIGURATION")
    public String configuration;

    public Configuration(long id, String configName, String description, String configuration) {
        this.id = id;
        this.configName = configName;
        this.description = description;
        this.configuration = configuration;
    }

    public Configuration() {
    }
}
