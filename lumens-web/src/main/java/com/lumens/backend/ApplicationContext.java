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
    private TransformEngine transformEngine;
    private ProjectContext projectContext;
    private OSResourcesMonitor osResourcesMonitor;
    private JobScheduler jobScheduler;
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
        transformEngine = new TransformEngine(classLoader);
        projectContext = new ProjectContext();
        jobScheduler = SchedulerFactory.get().createScheduler();
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
        this.transformEngine = engine;
        return this;
    }

    public TransformEngine getTransformEngine() {
        return this.transformEngine;
    }

    public OSResourcesMonitor getOSResourcesMonitor() {
        return this.osResourcesMonitor;
    }

    public JobScheduler getScheduler() {
        return this.jobScheduler;
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
        // Load the manage service, monitor must be created after jni path setting
        osResourcesMonitor = ServerManagementFactory.get().createOSResourcesMonitor();
        // Load the addin connectors
        transformEngine.start(ServerUtils.getNormalizedPath(getRealPath() + LUMENS_ADDIN));
        // Standby jobScheduler
        jobScheduler.startup();
    }

    public void stop() {
    }

}
