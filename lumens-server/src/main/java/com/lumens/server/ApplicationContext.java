/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.lumens.engine.TransformEngine;

/**
 * Hold all the application information
 */
public class ApplicationContext {

    private TransformEngine engine;
    private ProjectContext projectContext;

    public ApplicationContext() {
        engine = new TransformEngine();
        projectContext = new ProjectContext();
    }

    public ProjectContext getProjectContext() {
        return projectContext;
    }

    public ApplicationContext setProjectContext(ProjectContext projectContext) {
        this.projectContext = projectContext;
        return this;
    }

    public ApplicationContext setTransformEngine(TransformEngine engine) {
        this.engine = engine;
        return this;
    }

    public TransformEngine getTransformEngine() {
        return this.engine;
    }
}
