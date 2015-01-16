/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.entity;

import com.lumens.sysdb.Column;
import com.lumens.sysdb.Table;

@Table(name = "LUNS_PROJECT")
public class Project {

    @Column(name = "id")
    public long id;
    @Column(name = "name")
    public String name;
    @Column(name = "description")
    public String description;
    @Column(name = "data")
    public String data;

    public Project(long id, String name, String description, String content) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.data = content;
    }

    public Project() {
    }
}
