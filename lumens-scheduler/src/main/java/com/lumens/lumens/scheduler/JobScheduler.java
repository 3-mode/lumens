/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.lumens.scheduler;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface JobScheduler {
    public void resume();
    public void start();
    public void shutdown();
}
