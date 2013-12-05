/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/")
public class ApplicationService {

    @GET
    @Path("/project")
    @Produces("application/json")
    public Response getProject(@QueryParam("name") String name) {
        return Response.ok().entity(String.format("{ \"project\": %s }", name)).build();
    }

    @POST
    @Path("/project")
    @Produces("application/json")
    public Response createProject(String projectInfo) {
        return Response.ok().entity(String.format("{ \"project\": %s }", projectInfo)).build();
    }

    @GET
    @Path("/projects")
    @Produces("application/json")
    public Response getProjects(@QueryParam("page") int page) {
        return Response.ok().entity(String.format("{ \"projects\" : [{ \"project\": %d }] }", page)).build();
    }
}
