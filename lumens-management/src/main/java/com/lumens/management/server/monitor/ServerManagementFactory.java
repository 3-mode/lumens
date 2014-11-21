/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor;

import com.lumens.management.server.monitor.impl.ServerOSResourcesMonitor;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ServerManagementFactory {

    public static ServerManagementFactory get() {
        return new ServerManagementFactory();
    }

    public OSResourcesMonitor createOSResourcesMonitor(String jniPath) {
        System.setProperty("java.library.path", jniPath);
        return new ServerOSResourcesMonitor();
    }
}
