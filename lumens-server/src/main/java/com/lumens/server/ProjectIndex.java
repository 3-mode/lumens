/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

/**
 *
 * Hold the project name and description
 */
public class ProjectIndex {

    private String projectName;
    private String description;

    public ProjectIndex(String name, String description) {
        this.projectName = name;
        this.description = description;
    }

    public ProjectIndex(String name) {
        this(name, "");
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.projectName;
    }

    @Override
    public int hashCode() {
        return this.projectName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ProjectIndex)
            return this.projectName.equals(((ProjectIndex) o).getProjectName());
        return false;
    }
}
