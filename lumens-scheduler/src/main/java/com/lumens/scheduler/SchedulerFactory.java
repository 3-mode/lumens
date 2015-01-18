/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.scheduler.impl.DefaultScheduler;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SchedulerFactory {
    private static final class SchedulerFactoryHolder{
        private static final SchedulerFactory instance = new SchedulerFactory();
    }
    
    public static SchedulerFactory get() {
        return SchedulerFactoryHolder.instance;
    }

    private SchedulerFactory(){        
    }
    
    public JobScheduler createScheduler() {        
        return new DefaultScheduler();
    }
}
