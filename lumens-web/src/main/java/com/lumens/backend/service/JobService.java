/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/job")
public class JobService {

    @GET
    @Path("/")
    @Produces("application/json")
    public Response listJob() {
        return Response.ok().entity("{ 'do' : 'list' }").build();
    }

    @GET
    @Path("{jobId}")
    @Produces("application/json")
    public Response getOrExecuteJob(@PathParam("jobId") String jobId, @QueryParam("action") String action) {
        return Response.ok().entity(String.format("{ 'do' : 'get or execute job by [%s], action=[%s]' }", jobId, action)).build();
    }

    @POST
    @Path("/")
    @Produces("application/json")
    public Response createJob(String jobJSONString) {
        return Response.ok().entity("{ 'do' : 'create job' }").build();
    }

    @POST
    @Path("{jobId}")
    @Produces("application/json")
    public Response updateJob(@PathParam("jobId") String jobId, String jobJSONString) {
        return Response.ok().entity("{ 'do' : 'create job' }").build();
    }
}