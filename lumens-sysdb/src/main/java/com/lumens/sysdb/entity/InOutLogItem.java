/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.entity;

import com.lumens.sysdb.Column;
import com.lumens.sysdb.Table;
import java.sql.Timestamp;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Table(name = "LUNS_INOUTLOG")
public class InOutLogItem {
    @Column(name = "log_id")
    public long logID;
    @Column(name = "component_id")
    public long componentID;
    @Column(name = "component_name")
    public String componentName;
    @Column(name = "project_id")
    public long projectID;
    @Column(name = "project_name")
    public String projectName;
    @Column(name = "direction")
    public String direction;
    @Column(name = "target_name")
    public String targetName;
    @Column(name = "data")
    public String data;
    @Column(name = "last_modif_time")
    public Timestamp lastModifTime;
}
