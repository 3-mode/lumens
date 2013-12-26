/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

/**
 * Hold all the application information
 */
public class ApplicationContext {

    private ProjectContext projectContext = new ProjectContext();

    public ProjectContext getProjectContext() {
        return projectContext;
    }

    public ApplicationContext setProjectContext(ProjectContext projectContext) {
        this.projectContext = projectContext;
        return this;
    }
}
