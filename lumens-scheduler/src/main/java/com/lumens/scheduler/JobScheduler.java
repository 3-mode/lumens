/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.scheduler;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface JobScheduler {
    public void resume();
    public void schedule();
    public void start();
    public void stop();
}