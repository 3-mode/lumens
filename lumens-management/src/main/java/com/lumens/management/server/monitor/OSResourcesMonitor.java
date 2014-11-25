/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface OSResourcesMonitor {

    public String RESOURCES_SERVICE = "resources-monitor";

    public int getCpuUsage();

    public int getCpuCount();

    public Cpu[] gatherCpuPerc();

    public Memory getMemPerc();
}
