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

    private static String LUMENS_BASE = System.getProperty("lumens.base", "X:\\PRODUCT\\lumens\\dist\\lumens");
    private TransformEngine engine;
    private ProjectContext projectContext;
    private List<String> resultCache = new ArrayList<>();
    private String strRealPath;
    private static ApplicationContext context;

    public static void createInstance(ClassLoader classLoader) {
        // TODO
        System.out.println("Lumens Base: " + LUMENS_BASE);
        context = new ApplicationContext(classLoader, LUMENS_BASE);
        context.start();
    }

    public static ApplicationContext get() {
        return context;
    }

    public ApplicationContext(ClassLoader classLoader, String realPath) {
        engine = new TransformEngine(classLoader);
        projectContext = new ProjectContext();
        strRealPath = realPath;
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

    public String getRealPath() {
        return strRealPath;
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
        engine.start(getRealPath() + "/addin");
    }

    public void stop() {
    }
}
