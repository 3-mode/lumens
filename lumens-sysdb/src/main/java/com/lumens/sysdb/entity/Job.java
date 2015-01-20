/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.entity;

import com.lumens.sysdb.Column;
import com.lumens.sysdb.Table;
import java.util.Date;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
@Table(name = "LUNS_PROJECT")
public class Job {

    @Column(name = "id")
    public long id;
    @Column(name = "name")
    public String name;
    @Column(name = "description")
    public String description;
    @Column(name = "repeatMode")
    public int repeatMode;
    @Column(name = "startTime")
    public Date startTime;
    @Column(name = "endTime")
    public Date endTime;

    public Job(long id, String name, String description, int repeatMode, Date startTime, Date endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.repeatMode = repeatMode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Job() {
    }
}
