/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.run.ExecuteJob;
import java.util.ArrayList;
import java.util.List;
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

    private List<ExecuteJob> jobList = new ArrayList<>();
    private ThreadPoolExecutor threadPoolExecutor;
    private static AtomicInteger threadCounter = new AtomicInteger(1);

    protected class EngineThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TransformThread-" + threadCounter.getAndIncrement());
        }
    }

    public TransformEngine() {
        threadPoolExecutor = new ThreadPoolExecutor(1, 20, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(20), new EngineThreadFactory());
    }

    public TransformEngine(int maximumPoolSize/*Default 20 threads*/, long keepAliveSecondsTime/*Deault 30 seconds*/) {
        threadPoolExecutor = new ThreadPoolExecutor(1, maximumPoolSize, keepAliveSecondsTime, TimeUnit.SECONDS, new LinkedBlockingQueue(maximumPoolSize), new EngineThreadFactory());
    }

    public void execute(ExecuteJob job) throws Exception {
        // TODO if job is completed, it should be remove to finished list ???
        threadPoolExecutor.execute((Runnable) job);
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPoolExecutor;
    }
}
