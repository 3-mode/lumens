/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import com.lumens.management.server.monitor.Cpu;
import com.lumens.management.server.monitor.Disk;
import com.lumens.management.server.monitor.Memory;
import com.lumens.management.server.monitor.OSResourcesMonitor;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/server_resources")
public class ServerResourceService {
    @GET
    @Path("/cpu_count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCpuCount() {
        OSResourcesMonitor osMonitor = ApplicationContext.get().getOSResourcesMonitor();
        return Response.ok().entity(String.format("{ \"cpu_count\" : %d }", osMonitor.getCpuCount())).build();
    }

    @GET
    @Path("/cpu_perc")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCpuPerc() {
        OSResourcesMonitor osMonitor = ApplicationContext.get().getOSResourcesMonitor();
        int cpuCount = osMonitor.getCpuCount();
        Cpu[] cpus = osMonitor.gatherCpuPerc();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cpuCount; ++i) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(String.format("{ \"combined\"  : %d,", cpus[i].getCombined()));
            sb.append(String.format("  \"sys\" : %d,", cpus[i].getSys()));
            sb.append(String.format("  \"user\" : %d,", cpus[i].getUser()));
            sb.append(String.format("  \"idle\" : %d  }", cpus[i].getIdle()));
        }
        return Response.ok().entity(String.format("{ \"cpu_usage\": %d, \"cpu_perc_list\" : [ %s ] }", osMonitor.getCpuUsage(), sb.toString())).build();
    }

    @GET
    @Path("/mem_perc")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMemPerc() {
        OSResourcesMonitor osMonitor = ApplicationContext.get().getOSResourcesMonitor();
        Memory mem = osMonitor.getMemPerc();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{ \"used\"  : %d,", mem.getUsedMem()));
        sb.append(String.format("  \"free\" : %d,", mem.getFreeMem()));
        sb.append(String.format("  \"ram\" : %d  }", mem.getRAM()));
        return Response.ok().entity(String.format("{ \"memory\" : %s }", sb.toString())).build();
    }

    @GET
    @Path("/disk")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiskInfo() {
        OSResourcesMonitor osMonitor = ApplicationContext.get().getOSResourcesMonitor();
        Disk[] diskList = osMonitor.getDiskList();
        StringBuilder sb = new StringBuilder();
        for (Disk disk : diskList) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("{ \"name\": \"").append(disk.getDevName()).append("\",");
            sb.append("  \"total\": ").append(disk.getTotal() / 1024).append(",");
            sb.append("  \"use_perc\": ").append(disk.getUsePercent()).append(" }");
        }
        return Response.ok().entity(String.format("{ \"disk_list\" : [%s] }", sb.toString())).build();
    }

}
