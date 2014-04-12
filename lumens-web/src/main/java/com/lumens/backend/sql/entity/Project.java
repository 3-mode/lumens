/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.sql.entity;

import com.lumens.backend.sql.Column;
import com.lumens.backend.sql.Table;

@Table(name = "Project")
public class Project {

    @Column(name = "id")
    public String id;
    @Column(name = "name")
    public String name;
    @Column(name = "description")
    public String description;
    @Column(name = "data")
    public String data;

    public Project(String id, String name, String description, String content) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.data = content;
    }

    public Project() {
    }
}
