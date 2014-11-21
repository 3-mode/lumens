/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface OSResourcesMonitor {

    public int getCpuCount();

    public Cpu[] gatherCpuPerc();
}
