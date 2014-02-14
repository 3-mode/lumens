/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.lumens.engine.EngineContext;
import com.lumens.engine.TransformEngine;

/**
 * Hold all the application information
 */
public class ApplicationContext {

    private static String ADDIN_PATH = System.getProperty("lumens.addin", "addin");
    private TransformEngine engine;
    private ProjectContext projectContext;
    private String[] bundleLocations;

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

    public void init() {
        /*Map<String, String> config = ConfigUtil.createConfig();
         m_framework = createFramework(config);
         m_framework.init();
         m_framework.start();
         installAndStartBundles(bundleLocations);//*/
        EngineContext.init(new ConnectorFactoryHolderImpl());
    }

    public void clean() {
    }
}
