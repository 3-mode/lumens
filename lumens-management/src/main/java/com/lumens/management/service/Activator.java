/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.service;

import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.management.server.monitor.OSResourcesMonitor;
import com.lumens.management.server.monitor.ServerManagementFactory;
import java.util.Collections;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class Activator implements AddinActivator {

    private AddinContext addinContext;

    @Override
    public void start(AddinContext context) {
        addinContext = context;
        ServerManagementFactory.get().createOSResourcesMonitor();
        addinContext.registerService(OSResourcesMonitor.RESOURCES_SERVICE, ServerManagementFactory.get(), Collections.EMPTY_MAP);
    }

    @Override
    public void stop(AddinContext context) {
    }

}
