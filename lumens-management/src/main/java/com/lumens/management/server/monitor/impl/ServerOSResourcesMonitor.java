/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor.impl;

import com.lumens.management.server.monitor.Cpu;
import com.lumens.management.server.monitor.Memory;
import com.lumens.management.server.monitor.OSResourcesMonitor;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
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
                cpus[index++] = new Cpu(cpuPerc.getCombined(), cpuPerc.getSys(), cpuPerc.getUser(), cpuPerc.getIdle());
            }
            return cpus;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Memory getMemPerc() {
        try {
            Mem mem = sigarAPI.getMem();
            double total = mem.getTotal();
            double used = mem.getUsed();
            double usedPerc = used / total;
            return new Memory(usedPerc, 1 - usedPerc, mem.getTotal());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCpuUsage() {
        try {
            return (int) Math.rint(100 * sigarAPI.getCpuPerc().getCombined());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
