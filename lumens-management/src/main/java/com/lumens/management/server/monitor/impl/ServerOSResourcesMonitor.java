/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor.impl;

import com.lumens.management.server.monitor.Cpu;
import com.lumens.management.server.monitor.OSResourcesMonitor;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ServerOSResourcesMonitor implements OSResourcesMonitor {
    private final Sigar sigarAPI;

    public ServerOSResourcesMonitor() {
        sigarAPI = new Sigar();
    }

    @Override
    public int getCpuCount() {
        try {
            return sigarAPI.getCpuList().length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cpu[] gatherCpuPerc() {
        try {
            Cpu[] cpus = new Cpu[this.getCpuCount()];
            int index = 0;
            for (CpuPerc cpuPerc : sigarAPI.getCpuPercList()) {
                cpus[index++] = new Cpu(cpuPerc.getSys(), cpuPerc.getUser(), cpuPerc.getIdle());
            }
            return cpus;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
