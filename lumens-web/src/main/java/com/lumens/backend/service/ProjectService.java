/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import com.lumens.connector.Direction;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.run.LastResultHandler;
import com.lumens.engine.run.ResultHandler;
import com.lumens.engine.run.SingleThreadTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.io.JsonUtility;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.Pair;
import com.lumens.backend.ServerUtils;
import com.lumens.backend.sql.DAOFactory;
import com.lumens.backend.sql.dao.ProjectDAO;
import com.lumens.backend.sql.entity.Project;
import com.sun.jersey.api.client.ClientResponse.Status;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public static final String PROJECT_ACTIVE = "project-active";
    public static final String CONTENT = "content";
    public static final String ACTION = "action";
    public static final String CURRENT__EDITING__PROJECT = "Current_Editing_Project";
    @Context
    private ServletContext context;

    @POST
    @Path("{projectID}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response processProject(@PathParam("projectID") String projectID, String message, @Context HttpServletRequest req) {
        JsonNode messageJson = JsonUtility.createJson(message);
        JsonNode contentJson = messageJson.get(CONTENT);
        JsonNode actionJson = messageJson.get(ACTION);
        if (JsonUtility.isNotNull(actionJson)) {
            String action = actionJson.asText();
            if (PROJECT_CREATE.equalsIgnoreCase(action) && JsonUtility.isNotNull(contentJson))
                return createProject(contentJson);
            else if (PROJECT_UPDATE.equalsIgnoreCase(action) && JsonUtility.isNotNull(contentJson))
                return updateProject(projectID, contentJson);
            else if (PROJECT_EXECUTE.equalsIgnoreCase(action)) {
                return executeProjectJob(projectID);
            } else if (PROJECT_ACTIVE.equalsIgnoreCase(action)) {
                return activeProject(projectID, req);
            }
        }
        return Response.ok().entity(String.format("{ \"message\": %s }", "Fail")).build();
    }

    @POST
    @Consumes("application/json")
    public Response createProject(String message, @Context HttpServletRequest req) {
        JsonNode messageJson = JsonUtility.createJson(message);
        JsonNode contentJson = messageJson.get(CONTENT);
        JsonNode actionJson = messageJson.get(ACTION);
        if (JsonUtility.isNotNull(actionJson)) {
            String action = actionJson.asText();
            if (PROJECT_CREATE.equalsIgnoreCase(action) && JsonUtility.isNotNull(contentJson))
                return createProject(contentJson);
        }
        return Response.ok().entity(String.format("{ \"message\": %s }", "Fail")).build();
    }

    private Response executeProjectJob(String projectID) {
        try {
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
                        ApplicationContext.get().cacheResultString(baos.toString());
                    } catch (Exception ex) {
                    }
                }
            }
            List<ResultHandler> handlers = new ArrayList<>();
            handlers.add(new MyResultHandler());
            ApplicationContext.get().getTransformEngine().execute(new SingleThreadTransformExecuteJob(projectInstance, handlers));
            // TODO to run the project job
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeStringField("message", "executing is invoked");
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception ex) {
            return ServerUtils.getErrorMessageResponse(ex);
        }
    }

    private Response createProject(JsonNode contentJson) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(contentJson.asText().getBytes(UTF_8));
            TransformProject project = new TransformProject();
            new ProjectSerializer(project).readFromJson(bais);
            ProjectDAO pDAO = DAOFactory.getProjectDAO();
            String projectId = pDAO.create(new Project(ServerUtils.generateID("P"), project.getName(), project.getDescription(), contentJson.asText()));
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
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
            return ServerUtils.getErrorMessageResponse(ex);
        }
    }

    private Response updateProject(String projectID, JsonNode contentJson) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(contentJson.asText().getBytes(UTF_8));
            TransformProject project = new TransformProject();
            new ProjectSerializer(project).readFromJson(bais);
            ProjectDAO pDAO = DAOFactory.getProjectDAO();
            String projectId = pDAO.update(new Project(projectID, project.getName(), project.getDescription(), contentJson.asText()));
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
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
            return ServerUtils.getErrorMessageResponse(ex);
        }
    }

    private Response activeProject(String projectID, HttpServletRequest req) {
        Object attr = req.getSession().getAttribute(CURRENT__EDITING__PROJECT);
        if (attr != null) {
            try {
                Pair<String, TransformProject> pair = (Pair<String, TransformProject>) attr;
                pair.getSecond().open();
                JsonUtility utility = JsonUtility.createJsonUtility();
                JsonGenerator json = utility.getGenerator();
                json.writeStartObject();
                json.writeStringField("status", "OK");
                json.writeStringField("result_content", String.format("Project '%s' is actived successfully", pair.getSecond().getName()));
                json.writeEndObject();
                return Response.ok().entity(utility.toUTF8String()).build();
            } catch (Exception ex) {
                return ServerUtils.getErrorMessageResponse(ex);
            }
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity("No project is actived, please open the projet first!").build();
    }

    @GET
    @Path("/result")
    @Produces("application/json")
    public Response getProjectExecutionResults(@QueryParam("page") int page, @Context HttpServletRequest req) throws IOException {
        JsonUtility utility = JsonUtility.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeArrayFieldStart("Result");
        boolean isFirst = true;
        for (String result : ApplicationContext.get().getCacheResultString()) {
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
        try {
            // TODO
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        //
        JsonUtility utility = JsonUtility.createJsonUtility();
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
        JsonUtility utility = JsonUtility.createJsonUtility();
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
                ((Pair<String, TransformProject>) attr).getSecond().close();
            req.getSession().setAttribute(CURRENT__EDITING__PROJECT, new Pair<>(projectID, projectInstance));

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
            return ServerUtils.getErrorMessageResponse(ex);
        }
    }

    @GET
    @Path("{projectID}/format")
    @Produces("application/json")
    public Response getFormatFromComponent(@PathParam("projectID") String projectID,
                                           @QueryParam("component_name") String componentName,
                                           @QueryParam("format_name") String formatName,
                                           @QueryParam("format_path") String formatPath,
                                           @QueryParam("direction") String direction,
                                           @Context HttpServletRequest req) {
        if (componentName != null && direction != null) {
            Object attr = req.getSession().getAttribute(CURRENT__EDITING__PROJECT);
            if (attr != null) {
                Pair<String, TransformProject> pair = (Pair<String, TransformProject>) attr;
                TransformProject project = pair.getSecond();
                if (!pair.getFirst().equals(projectID))
                    return ServerUtils.getErrorMessageResponse(String.format("The project with id '%s' is not opened and actived", projectID));
                // To find the datasource format
                // TODO handle the uncode componentName
                try {
                    if (formatName != null) {

                        for (DataSource ds : project.getDatasourceList()) {
                            if (ds.getName().equals(componentName)) {
                                Direction direct = Direction.valueOf(direction);
                                Map<String, Format> formats = ds.getFormatList(direct);
                                Format requestedFormat = ds.getConnector().getFormat(formats.get(formatName), formatPath, direct);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                new FormatSerializer(requestedFormat).writeToJson(baos);
                                JsonUtility utility = JsonUtility.createJsonUtility();
                                JsonGenerator json = utility.getGenerator();
                                json.writeStartObject();
                                json.writeStringField("status", "OK");
                                json.writeObjectFieldStart("content");
                                json.writeArrayFieldStart("format_list");
                                json.writeRaw(baos.toString(UTF_8));
                                json.writeEndArray();
                                json.writeEndObject();
                                json.writeEndObject();
                                return Response.ok().entity(utility.toUTF8String()).build();
                            }
                        }

                    } else if (formatName == null) {
                        for (DataSource ds : project.getDatasourceList()) {
                            if (ds.getName().equals(componentName)) {
                                Map<String, Format> formats = ds.getFormatList(Direction.valueOf(direction));
                                Iterator<Map.Entry<String, Format>> it = formats.entrySet().iterator();
                                JsonUtility utility = JsonUtility.createJsonUtility();
                                JsonGenerator json = utility.getGenerator();
                                json.writeStartObject();
                                json.writeStringField("status", "OK");
                                json.writeObjectFieldStart("content");
                                // TODO json format
                                json.writeArrayFieldStart("format_list");
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                boolean isFirst = true;
                                while (it.hasNext()) {
                                    baos.reset();
                                    Format format = ((Map.Entry<String, Format>) it.next()).getValue();
                                    new FormatSerializer(format).writeToJson(baos);
                                    if (!isFirst)
                                        json.writeRaw(',');
                                    json.writeRaw(baos.toString(UTF_8));
                                    isFirst = false;
                                }
                                json.writeEndArray();
                                json.writeEndObject();
                                json.writeEndObject();
                                return Response.ok().entity(utility.toUTF8String()).build();
                            }
                        }
                    }
                } catch (Exception ex) {
                    return ServerUtils.getErrorMessageResponse(ex);
                }
            }
        }
        return Response.ok().entity("Not implemented").build();
    }
}
