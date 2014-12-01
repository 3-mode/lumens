/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor;

import com.lumens.management.server.monitor.jni.NavtiveLibraryScope;
import com.lumens.management.server.monitor.impl.ServerOSResourcesMonitor;
import org.hyperic.sigar.SigarLoader;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ServerManagementFactory {

    public static ServerManagementFactory get() {
        return new ServerManagementFactory();
    }

    public OSResourcesMonitor createOSResourcesMonitor() {
        NavtiveLibraryScope.removeAll(SigarLoader.class.getClassLoader());
        return new ServerOSResourcesMonitor();
    }
}
