/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

/**
 *
 * Hold the project id and description
 */
public class ProjectIndex {

    private String id;
    private String description;

    public ProjectIndex(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public ProjectIndex(String name) {
        this(name, "");
    }

    /**
     * @return the projectName
     */
    public String getProjectID() {
        return id;
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
        return this.id;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ProjectIndex)
            return this.id.equals(((ProjectIndex) o).getProjectID());
        return false;
    }
}
