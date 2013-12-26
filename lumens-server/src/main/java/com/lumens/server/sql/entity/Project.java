/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server.sql.entity;

import com.lumens.server.sql.Column;
import com.lumens.server.sql.Table;

@Table(name = "Project")
public class Project {

    @Column(name = "id")
    public String Id;
    @Column(name = "name")
    public String name;
    @Column(name = "description")
    public String description;
    @Column(name = "content")
    public String data;

    public Project(String Id, String name, String description, String content) {
        this.Id = Id;
        this.name = name;
        this.description = description;
        this.data = content;
    }

    public Project() {
    }
}
