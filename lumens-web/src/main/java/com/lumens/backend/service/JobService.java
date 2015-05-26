/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import com.lumens.backend.ServerUtils;
import static com.lumens.backend.ServiceConstants.CONTENT;
import com.lumens.io.JsonUtility;
import com.lumens.scheduler.JobConfigurationBuilder;
import com.lumens.scheduler.JobConfiguration;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.dao.JobProjectRelationDAO;
import com.lumens.sysdb.entity.Job;
import com.lumens.sysdb.entity.Project;
import com.lumens.engine.DBHelper;
import com.lumens.logsys.JobLogFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response listJob() {
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
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

    @DELETE
    @Path("{jobId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJob(@PathParam("jobId") long jobId) {
        try {
            JobDAO jobDAO = DAOFactory.getJobDAO();
            jobDAO.delete(jobId);
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            return ServerUtils.getErrorMessageResponse(e);
        }
    }

    @GET
    @Path("{jobId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrExecuteJob(@PathParam("jobId") String jobId, @QueryParam("action") String action) {
        try {
            String message = "";
            if ("start".equalsIgnoreCase(action)) {
                JobDAO jobDAO = DAOFactory.getJobDAO();
                long lJobId = Long.parseLong(jobId);
                Job job = jobDAO.getJob(lJobId);
                JobConfiguration jc = JobConfigurationBuilder.build(job, JobLogFactory.create(ApplicationContext.get().getRealPath(), lJobId));
                jc.addProject(DBHelper.loadTransformProjectFromDb(lJobId));
                jc.verfiyAssociatedProjects();
                ApplicationContext.get().getScheduler().addSchedule(jc);
                ApplicationContext.get().getScheduler().startJob(lJobId);
                message = "Started successfully";
            } else if ("stop".equalsIgnoreCase(action)) {
                long lJobId = Long.parseLong(jobId);
                ApplicationContext.get().getScheduler().stopJob(lJobId);
                ApplicationContext.get().getScheduler().deleteJob(lJobId);
                message = "Stop successfully";
            }
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
            json.writeStringField("do", message + "; " + String.format("get or execute job by [%s]", jobId));
            json.writeStringField("action", String.format("[%s]", action));
            json.writeEndObject();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            return ServerUtils.getErrorMessageResponse(e);
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response createJob(String message) {
        JsonNode messageJson = JsonUtility.createJson(message);
        JobDAO jobDAO = DAOFactory.getJobDAO();
        long saveId = jobDAO.create(getJobDBEntity(messageJson));
        List<Long> projectIdList = getProjectList(messageJson);
        JobProjectRelationDAO jprDAO = DAOFactory.getRelationDAO();
        for (long projectId : projectIdList)
            jprDAO.create(saveId, projectId);
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJob(@PathParam("jobId") String jobId, String message) {
        JsonNode messageJson = JsonUtility.createJson(message);
        JobDAO jobDAO = DAOFactory.getJobDAO();
        long saveId = jobDAO.update(getJobDBEntity(messageJson));
        List<Long> projectIdList = getProjectList(messageJson);
        JobProjectRelationDAO jprDAO = DAOFactory.getRelationDAO();
        jprDAO.deleteAllRelation(saveId);
        for (long projectId : projectIdList)
            jprDAO.create(saveId, projectId);
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

    private Job getJobDBEntity(JsonNode messageJson) {
        JsonNode contentJson = messageJson.get(CONTENT);
        return new Job(Long.parseLong(contentJson.get("id").asText()),
                       contentJson.get("name").asText(),
                       contentJson.get("description").asText(),
                       Integer.parseInt(contentJson.get("repeat_mode").asText()),
                       Integer.parseInt(contentJson.get("interval").asText()),
                       ServerUtils.getTimestampFromString(contentJson.get("start_time").asText()).getTime(), 0L);
    }

    private List<Long> getProjectList(JsonNode messageJson) {
        List<Long> list = new ArrayList<>();
        JsonNode contentJson = messageJson.get(CONTENT);
        JsonNode projectsJson = contentJson.get("projects");
        if (projectsJson != null) {
            Iterator<JsonNode> it = projectsJson.getElements();
            while (it.hasNext()) {
                JsonNode project = it.next();
                list.add(Long.parseLong(project.get("project_id").asText()));
            }
        }
        return list;
    }
}
