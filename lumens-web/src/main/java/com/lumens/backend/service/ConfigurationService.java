/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/config")
public class ConfigurationService {
    @GET
    @Path("/job/log")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJobLogLevelConfig() {
        return Response.ok().build();
    }

    @POST
    @Path("/job/log/{config_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveJobLogLevelConfig() {
        return Response.ok().build();
    }
}
