/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.engine.TransformEngine;
import com.lumens.scheduler.*;
import com.lumens.management.server.monitor.OSResourcesMonitor;
import com.lumens.management.server.monitor.ServerManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Hold all the application information
 */
public class ApplicationContext {

    public static String LUMENS_BASE = System.getProperty("lumens.base", "../dist/lumens");
    public static String LUMENS_ADDIN = "/addin";
    public static String LUMENS_JNI = "/module/manage/jni";
    private final List<String> resultCache = new ArrayList<>();
    private final String strRealPath;
    private TransformEngine engine;
    private ProjectContext projectContext;
    private OSResourcesMonitor osResourcesMonitor;
    private JobScheduler scheduler;
    private static ApplicationContext context;

    public static void createInstance(ClassLoader classLoader) {
        // TODO log
        System.out.println("Lumens Base: " + LUMENS_BASE);
        context = new ApplicationContext(LUMENS_BASE, classLoader);
        context.start();
    }

    public static ApplicationContext get() {
        return context;
    }

    public ApplicationContext(String realPath, ClassLoader classLoader) {
        System.out.println("Application Context is initializing ...");
        strRealPath = realPath;
        engine = new TransformEngine(classLoader);
        projectContext = new ProjectContext();
        System.out.println("Application Context completed initializing .");
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

    public OSResourcesMonitor getOSResourcesMonitor() {
        return this.osResourcesMonitor;
    }

    public JobScheduler getScheduler() {
        return this.scheduler;
    }

    public String getRealPath() {
        return strRealPath;
    }

    public synchronized void cacheResultString(String result) {
        if (resultCache.size() < 20) {
            resultCache.add(result);
        } else if (resultCache.size() >= 20) {
            resultCache.set(resultCache.size() % 20, result);
        }
    }

    public synchronized String[] getCacheResultString() {
        String[] results = new String[resultCache.size()];
        return resultCache.toArray(results);
    }

    public void start() {

        // Initialize the JNI path when starting
        System.setProperty("java.library.path", ServerUtils.getNormalizedPath(getRealPath() + LUMENS_JNI));
        // Load the addin connectors
        engine.start(ServerUtils.getNormalizedPath(getRealPath() + LUMENS_ADDIN));
        // Load the manage service
        osResourcesMonitor = ServerManagementFactory.get().createOSResourcesMonitor();
        scheduler = SchedulerFactory.get().createScheduler();
        scheduler.startup(); 
    }

    public void stop() {
    }

}
