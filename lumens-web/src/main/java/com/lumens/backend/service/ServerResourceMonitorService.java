/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import com.lumens.management.server.monitor.Cpu;
import com.lumens.management.server.monitor.OSResourcesMonitor;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/server_resources")
public class ServerResourceMonitorService {
    @GET
    @Produces("application/json")
    @Path("/cpu_count")
    public Response listCpuCount() {
        OSResourcesMonitor osMonitor = ApplicationContext.get().getOSResourcesMonitor();
        return Response.ok().entity(String.format("{ 'cpu_count' : %d }", osMonitor.getCpuCount())).build();
    }

    @GET
    @Produces("application/json")
    @Path("/cpu_perc")
    public Response listCpuPerc() {
        OSResourcesMonitor osMonitor = ApplicationContext.get().getOSResourcesMonitor();
        int cpuCount = osMonitor.getCpuCount();
        Cpu[] cpus = osMonitor.gatherCpuPerc();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cpuCount; ++i) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(String.format("{ 'sys'  : %d,", cpus[i].getSys()));
            sb.append(String.format("  'user' : %d,", cpus[i].getUser()));
            sb.append(String.format("  'idle' : %d  }", cpus[i].getIdle()));
        }
        return Response.ok().entity(String.format("{ "
                                                  + "'cpu_perc_list' : [ %s ]"
                                                  + "}", sb.toString())).build();
    }

}
