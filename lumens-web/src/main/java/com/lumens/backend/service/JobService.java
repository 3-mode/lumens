/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ServerUtils;
import static com.lumens.backend.ServiceConstants.CONTENT;
import com.lumens.io.JsonUtility;
import com.lumens.model.DateTime;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.entity.Job;
import com.lumens.sysdb.entity.Project;
import com.lumens.sysdb.utils.DBHelper;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

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
                json.writeNumberField("repeat_mode", job.repeat);
                json.writeNumberField("interval", job.interval);
                json.writeStringField("start_time", ServerUtils.getJobStartTimeString(job.startTime));
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

    @PUT
    @Produces("application/json")
    public Response createJob(String message) {
        JsonNode messageJson = JsonUtility.createJson(message);
        JsonNode contentJson = messageJson.get(CONTENT);
        JobDAO jobDAO = DAOFactory.getJobDAO();
        Job job = new Job(contentJson.get("id").getLongValue(),
                          contentJson.get("name").getTextValue(),
                          contentJson.get("description").getTextValue(),
                          contentJson.get("repeat_mode").getIntValue(),
                          contentJson.get("interval").getIntValue(),
                          ServerUtils.getTimestampFromString(contentJson.get("start_time").getTextValue()).getTime(), 0L);
        long saveId = jobDAO.create(job);
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
            json.writeNumberField("job_id", saveId);
            json.writeEndObject();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            return ServerUtils.getErrorMessageResponse(e);
        }
    }

    @POST
    @Path("{jobId}")
    @Produces("application/json")
    public Response updateJob(@PathParam("jobId") String jobId, String message) {
        JsonNode messageJson = JsonUtility.createJson(message);
        JsonNode contentJson = messageJson.get(CONTENT);
        JobDAO jobDAO = DAOFactory.getJobDAO();
        Job job = new Job(Long.parseLong(contentJson.get("id").getTextValue()),
                          contentJson.get("name").getTextValue(),
                          contentJson.get("description").getTextValue(),
                          contentJson.get("repeat_mode").getIntValue(),
                          contentJson.get("interval").getIntValue(),
                          ServerUtils.getTimestampFromString(contentJson.get("start_time").getTextValue()).getTime(), 0L);
        long saveId = jobDAO.update(job);
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
            json.writeNumberField("job_id", saveId);
            json.writeEndObject();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            return ServerUtils.getErrorMessageResponse(e);
        }
    }
}
