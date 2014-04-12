/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.engine.TransformEngine;
import java.util.ArrayList;
import java.util.List;

/**
 * Hold all the application information
 */
public class ApplicationContext {

    private static String ADDIN_PATH = System.getProperty("lumens.addin", "addin");
    private TransformEngine engine;
    private ProjectContext projectContext;
    public List<String> resultCache = new ArrayList<>();
    private static ApplicationContext context;

    public static void createInstance() {
        context = new ApplicationContext();
    }

    public static ApplicationContext get() {
        return context;
    }

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

    public synchronized void cacheResultString(String result) {
        if (resultCache.size() < 20)
            resultCache.add(result);
        else if (resultCache.size() >= 20) {
            resultCache.set(resultCache.size() % 20, result);
        }
    }

    public synchronized String[] getCacheResultString() {
        String[] results = new String[resultCache.size()];
        return resultCache.toArray(results);
    }

    public void start() {
        engine.start(ADDIN_PATH);
    }

    public void stop() {
    }
}
