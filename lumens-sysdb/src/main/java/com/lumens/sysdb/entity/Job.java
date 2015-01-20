/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.entity;

import com.lumens.sysdb.Column;
import com.lumens.sysdb.Table;
import java.sql.Timestamp;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
@Table(name = "LUNS_JOB")
public class Job {

    @Column(name = "id")
    public long id;
    @Column(name = "name")
    public String name;
    @Column(name = "description")
    public String description;
    @Column(name = "repeat_count")
    public int repeatCount;
    @Column(name = "interval")
    public int interval;    
    @Column(name = "start_time")
    public Timestamp startTime;
    @Column(name = "end_time")
    public Timestamp endTime;

    public Job(long id, String name, String description, int repeatCount, int interval, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.repeatCount = repeatCount;
        this.interval = interval;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Job() {
    }
}
