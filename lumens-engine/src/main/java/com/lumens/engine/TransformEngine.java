/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.LumensException;
import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.engine.run.ExecuteJob;
import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformEngine {

    private AddinEngine addinEngine;
    private ThreadPoolExecutor threadPoolExecutor;
    private ClassLoader addinClassLoader;
    private static AtomicInteger threadCounter = new AtomicInteger(1);

    protected class EngineThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TransformThread-" + threadCounter.getAndIncrement());
        }
    }

    public TransformEngine() {
        this(TransformEngine.class.getClassLoader());
    }

    public TransformEngine(ClassLoader addinClassLoader) {
        this.addinClassLoader = addinClassLoader;
        threadPoolExecutor = new ThreadPoolExecutor(1, 20, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(20), new EngineThreadFactory());
    }

    public TransformEngine(ClassLoader addinClassLoader, int maximumPoolSize/*Default 20 threads*/, long keepAliveSecondsTime/*Deault 30 seconds*/) {
        this.addinClassLoader = addinClassLoader;
        threadPoolExecutor = new ThreadPoolExecutor(1, maximumPoolSize, keepAliveSecondsTime, TimeUnit.SECONDS, new LinkedBlockingQueue(maximumPoolSize), new EngineThreadFactory());
    }

    public void execute(ExecuteJob job) throws Exception {
        // TODO if job is completed, it should be remove to finished list ???
        threadPoolExecutor.execute((Runnable) job);
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPoolExecutor;
    }

    public void start(String addinPath) {
        try {
            addinEngine = new AddinEngine(addinClassLoader);
            addinEngine.start();
            AddinContext ac = addinEngine.getAddinContext();
            System.out.println("Addin path: " + addinPath);
            File addinPathFile = new File(addinPath);
            for (File addinItemFile : addinPathFile.listFiles()) {
                ac.installAddIn(addinItemFile.toURI().toURL()).start();
            }
            TransformEngineContext.start(new DefaultConnectorFactoryHolder(ac));
        } catch (MalformedURLException ex) {
            throw new LumensException(ex);
        }
    }

    public void start(AddinEngine addinEngine) {
        this.addinEngine = addinEngine;
    }

    public AddinEngine getAddinEngine() {
        return this.addinEngine;
    }
}
