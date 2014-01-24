/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server.service;

import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.run.LastResultHandler;
import com.lumens.engine.run.ResultHandler;
import com.lumens.engine.run.SingleThreadTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.server.Application;
import com.lumens.server.JsonUtility;
import com.lumens.server.ServerUtils;
import com.lumens.server.sql.DAOFactory;
import com.lumens.server.sql.dao.ProjectDAO;
import com.lumens.server.sql.entity.Project;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

@Path("/projects")
public class ProjectService {

    public static final String UTF_8 = "UTF-8";
    public static final String PROJECT_CREATE = "project-create";
    public static final String PROJECT_UPDATE = "project-update";
    public static final String PROJECT_EXECUTE = "project-execute";
    public static final String CONTENT = "content";
    public static final String ACTION = "action";
    public static final String CURRENT__EDITING__PROJECT = "Current_Editing_Project";
    @Context
    private ServletContext context;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response processProject(String message, @Context HttpServletRequest req) {
        JsonNode messageJson = ServerUtils.createJson(message);
        JsonNode contentJson = messageJson.get(CONTENT);
        JsonNode actionJson = messageJson.get(ACTION);
        if (ServerUtils.isNotNull(actionJson) && ServerUtils.isNotNull(contentJson)) {
            String action = actionJson.asText();
            if (PROJECT_CREATE.equalsIgnoreCase(action))
                return createProject(contentJson);
            else if (PROJECT_UPDATE.equalsIgnoreCase(action))
                return updateProject(contentJson);
            else if (PROJECT_EXECUTE.equalsIgnoreCase(action)) {
                return executeProjectJob(contentJson);
            }
        }
        return Response.ok().entity(String.format("{ \"message\": %s }", "Fail")).build();
    }

    private Response executeProjectJob(JsonNode contentJson) {
        try {
            String projectID = contentJson.get("project_id").asText().trim();
            ProjectDAO pDAO = DAOFactory.getProjectDAO();
            Project project = pDAO.getProject(projectID);
            TransformProject projectInstance = new TransformProject();
            new ProjectSerializer(projectInstance).readFromJson(new ByteArrayInputStream(project.data.getBytes()));
            // Execute all start rules to drive the ws connector
            class MyResultHandler implements LastResultHandler {

                @Override
                public void process(TransformComponent src, String resultName, List<Element> results) {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        new ElementSerializer(results.get(0), true).writeToJson(baos);
                        Application.getInstance().cacheResultString(baos.toString());
                    } catch (Exception ex) {
                    }
                }
            }
            List<ResultHandler> handlers = new ArrayList<>();
            handlers.add(new MyResultHandler());
            Application.getInstance().getApplicationContext().getTransformEngine().execute(new SingleThreadTransformExecuteJob(projectInstance, handlers));
            // TODO to run the project job
            JsonUtility utility = ServerUtils.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeStringField("message", "executing is invoked");
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception ex) {
            return getErrorMessageResponse(ex);
        }
    }

    private Response createProject(JsonNode contentJson) {
        JsonUtility utility = ServerUtils.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(contentJson.asText().getBytes(UTF_8));
            TransformProject project = new TransformProject();
            new ProjectSerializer(project).readFromJson(bais);
            ProjectDAO pDAO = DAOFactory.getProjectDAO();
            String projectId = pDAO.create(new Project(ServerUtils.generateID("P"), project.getName(), project.getDescription(), contentJson.asText()));
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
            json.writeArrayFieldStart("project");
            json.writeStartObject();
            json.writeStringField("id", projectId);
            json.writeStringField("name", project.getName());
            json.writeStringField("description", project.getDescription());
            json.writeEndObject();
            json.writeEndArray();
            json.writeEndObject();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception ex) {
            return getErrorMessageResponse(ex);
        }
    }

    private Response updateProject(JsonNode contentJson) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @GET
    @Path("/result")
    @Produces("application/json")
    public Response getProjectExecutionResults(@QueryParam("page") int page, @Context HttpServletRequest req) throws IOException {
        JsonUtility utility = ServerUtils.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeArrayFieldStart("Result");
        boolean isFirst = true;
        for (String result : Application.getInstance().getCacheResultString()) {
            if (!isFirst)
                json.writeRaw(',');
            else
                isFirst = false;
            json.writeRaw(result);
        }
        json.writeEndArray();
        json.writeEndObject();
        return Response.ok().entity(utility.toUTF8String()).build();
    }

    @GET
    @Produces("application/json")
    public Response getProjects(@QueryParam("page") int page, @Context HttpServletRequest req) throws IOException {
        JsonUtility utility = ServerUtils.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeStringField("status", "OK");
        json.writeObjectFieldStart("content");
        json.writeArrayFieldStart("project");
        // Project list
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        for (Project project : pDAO.getAllShortProject()) {
            json.writeStartObject();
            json.writeStringField("id", project.id);
            json.writeStringField("name", project.name);
            json.writeStringField("description", project.description);
            json.writeEndObject();
        }
        json.writeEndArray();
        json.writeEndObject();
        json.writeEndObject();
        return Response.ok().entity(utility.toUTF8String()).build();
    }

    @GET
    @Path("{projectID}")
    @Produces("application/json")
    public Response getProjectByID(@PathParam("projectID") String projectID, @Context HttpServletRequest req) throws IOException {
        JsonUtility utility = ServerUtils.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeStringField("status", "OK");
        json.writeObjectFieldStart("content");
        json.writeArrayFieldStart("project");
        // Project
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        Project project = pDAO.getProject(projectID);
        TransformProject projectInstance = new TransformProject();
        try {
            new ProjectSerializer(projectInstance).readFromJson(new ByteArrayInputStream(project.data.getBytes()));
            Object attr = req.getSession().getAttribute(CURRENT__EDITING__PROJECT);
            if (attr != null)
                ((TransformProject) attr).close();
            req.getSession().getServletContext().setAttribute(CURRENT__EDITING__PROJECT, projectInstance);

            json.writeStartObject();
            json.writeStringField("id", project.id);
            json.writeStringField("name", project.name);
            json.writeStringField("description", project.description);
            json.writeStringField("data", project.data);
            json.writeEndObject();
            json.writeEndArray();
            json.writeEndObject();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception ex) {
            return getErrorMessageResponse(ex);
        }
    }

    @GET
    @Path("{projectID}/format")
    @Produces("application/json")
    public Response getFormatFromComponent(@QueryParam("component_name") String componentName, @QueryParam("format_name") String formatName, @Context HttpServletRequest req) {
        if (componentName != null && formatName != null)
            return Response.ok().entity("ok to get connector's format").build();
        else if (componentName != null && formatName == null)
            return Response.ok().entity("ok to get connector's format list").build();
        return Response.ok().entity("Not implemented").build();
    }

    public Response getErrorMessageResponse(Exception ex) {
        try {
            JsonUtility utility = ServerUtils.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "Failed");
            json.writeStringField("error_message", ex.toString());
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
