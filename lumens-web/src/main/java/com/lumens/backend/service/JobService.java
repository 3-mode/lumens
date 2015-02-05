/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ServerUtils;
import com.lumens.io.JsonUtility;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.entity.Job;
import com.lumens.sysdb.entity.Project;
import com.lumens.sysdb.utils.DBHelper;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/job")
public class JobService {

    @GET
    @Produces("application/json")
    public Response listJob() {
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("content");
            json.writeArrayFieldStart("jobs");
            JobDAO jobDAO = DAOFactory.getJobDAO();
            List<Job> jobList = jobDAO.getAllJob();
            for (Job job : jobList) {
                json.writeStartObject();
                json.writeStringField("id", Long.toString(job.id));
                json.writeStringField("name", job.name);
                json.writeStringField("description", job.description);
                json.writeArrayFieldStart("projects");
                List<Project> projectList = DBHelper.loadShortProjectFromDb(job.id);
                for (Project project : projectList) {
                    json.writeStartObject();
                    json.writeNumberField("project_id", project.id);
                    json.writeStringField("project_name", project.name);
                    json.writeStringField("project_description", project.description);
                    json.writeEndObject();
                }
                json.writeEndArray();
                json.writeEndObject();
            }
            json.writeEndArray();
            json.writeEndObject();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            return ServerUtils.getErrorMessageResponse(e);
        }
    }

    @GET
    @Path("{jobId}")
    @Produces("application/json")
    public Response getOrExecuteJob(@PathParam("jobId") String jobId, @QueryParam("action") String action) {
        return Response.ok().entity(String.format("{ 'do' : 'get or execute job by [%s], action=[%s]' }", jobId, action)).build();
    }

    @POST
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
