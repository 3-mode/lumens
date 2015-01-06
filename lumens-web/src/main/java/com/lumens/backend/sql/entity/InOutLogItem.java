/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.sql.entity;

import com.lumens.backend.sql.Column;
import com.lumens.backend.sql.Table;
import java.sql.Timestamp;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Table(name = "LUNS_INOUTLOG")
public class InOutLogItem {
    @Column(name = "component_id")
    public long componentID;
    @Column(name = "project_id")
    public long projectID;
    @Column(name = "direction")
    public String direction;
    @Column(name = "target_name")
    public String targetName;
    @Column(name = "data")
    public String data;
    @Column(name = "last_modif_time")
    public Timestamp lastModifTime;
}
