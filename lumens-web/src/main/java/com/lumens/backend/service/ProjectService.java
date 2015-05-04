/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import com.lumens.connector.Direction;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.io.JsonUtility;
import com.lumens.io.Utils;
import com.lumens.model.Format;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.Pair;
import com.lumens.backend.ServerUtils;
import com.lumens.backend.ServiceConstants;
import static com.lumens.backend.ServiceConstants.ACTIVE;
import static com.lumens.backend.ServiceConstants.DELETE;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.InOutLogDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.entity.InOutLogItem;
import com.lumens.sysdb.entity.Project;
import com.sun.jersey.api.client.ClientResponse.Status;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

@Path("/project")
public class ProjectService implements ServiceConstants {

    @Context
    private ServletContext context;

    @POST
    @Path("{projectID}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response processProject(@PathParam("projectID") long projectID, String message, @Context HttpServletRequest req) {
        try {
            JsonNode messageJson = JsonUtility.createJson(message);
            JsonNode contentJson = messageJson.get(CONTENT);
            JsonNode actionJson = messageJson.get(ACTION);
            if (JsonUtility.isNotNull(actionJson)) {
                String action = actionJson.asText();
                if (CREATE.equalsIgnoreCase(action) && JsonUtility.isNotNull(contentJson))
                    return createProject(contentJson);
                else if (UPDATE.equalsIgnoreCase(action) && JsonUtility.isNotNull(contentJson))
                    return updateProject(projectID, contentJson);
                else if (DELETE.equalsIgnoreCase(action))
                    return deleteProject(projectID, req);
                else if (CLOSE.equalsIgnoreCase(action))
                    return closeProject(projectID, req);
                else if (DEPLOY.equalsIgnoreCase(action))
                    return deployProject(projectID, req);
                else if (ACTIVE.equalsIgnoreCase(action))
                    return activeProject(projectID, req);
                else if (EXECUTE.equalsIgnoreCase(action))
                    return executeProjectJob(projectID);
            }
        } catch (Exception ex) {
            return ServerUtils.getErrorMessageResponse(ex);
        }
        return ServerUtils.getErrorMessageResponse("Not supported [" + message + "]");
    }

    @POST
    @Consumes("application/json")
    public Response createProject(String message, @Context HttpServletRequest req) throws Exception {
        JsonNode messageJson = JsonUtility.createJson(message);
        JsonNode contentJson = messageJson.get(CONTENT);
        JsonNode actionJson = messageJson.get(ACTION);
        if (JsonUtility.isNotNull(actionJson)) {
            String action = actionJson.asText();
            if (CREATE.equalsIgnoreCase(action) && JsonUtility.isNotNull(contentJson))
                return createProject(contentJson);
        }
        return ServerUtils.getErrorMessageResponse("Unkown error");
    }

    private Response executeProjectJob(long projectID) throws Exception {
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        Project project = pDAO.getProject(projectID);
        TransformProject projectInstance = new TransformProject();
        new ProjectSerializer(projectInstance).readFromJson(new ByteArrayInputStream(project.data.getBytes()));
        // Execute all start rules to drive the ws connector
        List<ResultHandler> handlers = new ArrayList<>();
        ApplicationContext.get().getTransformEngine().execute(new SequenceTransformExecuteJob(projectInstance, handlers));
        // TODO to run the project job
        JsonUtility utility = JsonUtility.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeStringField("status", "OK");
        json.writeStringField("result_content", "executing is invoked");
        json.writeEndObject();
        return Response.ok().entity(utility.toUTF8String()).build();
    }

    private Response createProject(JsonNode contentJson) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(contentJson.asText().getBytes(UTF_8));
        TransformProject project = new TransformProject();
        // Load it first to verify the project
        new ProjectSerializer(project).readFromJson(bais);
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        long projectId = pDAO.create(new Project(Utils.generateID(), project.getName(), project.getDescription(), contentJson.asText()));
        JsonUtility utility = JsonUtility.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeStringField("status", "OK");
        json.writeObjectFieldStart("result_content");
        json.writeArrayFieldStart("project");
        json.writeStartObject();
        json.writeNumberField("id", projectId);
        json.writeStringField("name", project.getName());
        json.writeStringField("description", project.getDescription());
        json.writeEndObject();
        json.writeEndArray();
        json.writeEndObject();
        json.writeEndObject();
        return Response.ok().entity(utility.toUTF8String()).build();
    }

    private Response updateProject(long projectID, JsonNode contentJson) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(contentJson.asText().getBytes(UTF_8));
        TransformProject project = new TransformProject();
        ProjectSerializer projSerial = new ProjectSerializer(project);
        projSerial.readFromJson(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        projSerial.writeToJson(baos);
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        long projectId = pDAO.update(new Project(projectID, project.getName(), project.getDescription(), baos.toString()));
        JsonUtility utility = JsonUtility.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeStringField("status", "OK");
        json.writeObjectFieldStart("result_content");
        json.writeArrayFieldStart("project");
        json.writeStartObject();
        json.writeNumberField("id", projectId);
        json.writeStringField("name", project.getName());
        json.writeStringField("description", project.getDescription());
        json.writeEndObject();
        json.writeEndArray();
        json.writeEndObject();
        json.writeEndObject();
        return Response.ok().entity(utility.toUTF8String()).build();
    }

    private Response activeProject(long projectID, HttpServletRequest req) {
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
    @Path("/testexec/log")
    @Produces("application/json")
    public Response getProjectExecutionResults(@QueryParam("project_id") long projectID, @QueryParam("component_id") long componentID) throws IOException {
        try {
            return this.getProjectTestExecResult(projectID, componentID);
        } catch (Exception ex) {
            return ServerUtils.getErrorMessageResponse(ex);
        }
    }

    @DELETE
    @Path("/testexec/log")
    @Produces("application/json")
    public Response deleteProjectExecutionResults(@QueryParam("project_id") long projectID) throws IOException {
        try {
            InOutLogDAO inoutLogDAO = DAOFactory.getInOutLogDAO();
            inoutLogDAO.deleteAllLog(projectID);
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception ex) {
            return ServerUtils.getErrorMessageResponse(ex);
        }
    }

    @GET
    @Produces("application/json")
    public Response listProject(@QueryParam("page") int page, @Context HttpServletRequest req) throws IOException {
        try {
            // TODO test loading icon
            Thread.sleep(500);
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
            json.writeNumberField("id", project.id);
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
    public Response getProject(@PathParam("projectID") long projectID, @Context HttpServletRequest req) throws IOException {
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
                ((Pair<Long, TransformProject>) attr).getSecond().close();
            req.getSession().setAttribute(CURRENT__EDITING__PROJECT, new Pair<>(projectID, projectInstance));

            json.writeStartObject();
            json.writeNumberField("id", project.id);
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
    public Response getFormatFromComponent(@PathParam("projectID") long projectID,
                                           @QueryParam("component_id") String componentId,
                                           @QueryParam("format_name") String formatName,
                                           @QueryParam("format_path") String formatPath,
                                           @QueryParam("direction") String direction,
                                           @Context HttpServletRequest req
    ) {
        if (componentId != null && direction != null) {
            Object attr = req.getSession().getAttribute(CURRENT__EDITING__PROJECT);
            if (attr != null) {
                Pair<Long, TransformProject> pair = (Pair<Long, TransformProject>) attr;
                TransformProject project = pair.getSecond();
                if (project == null || pair.getFirst() != projectID)
                    return ServerUtils.getErrorMessageResponse(String.format("The project with id '%s' is not opened", projectID));
                else if (!project.isOpen())
                    return ServerUtils.getErrorMessageResponse(String.format("The project '%s' is not actived", project.getName()));
                // To find the datasource format
                // TODO handle the uncode componentName
                try {
                    DataSource requiredDs = null;
                    for (DataSource ds : project.getDatasourceList()) {
                        if (ds.getId().equals(componentId)) {
                            requiredDs = ds;
                            break;
                        }
                    }
                    Direction direct = Direction.valueOf(direction);
                    JsonUtility utility = JsonUtility.createJsonUtility();
                    JsonGenerator json = utility.getGenerator();
                    Map<String, Format> formats = requiredDs.getFormatList(direct);
                    if (formatName == null) {
                        json.writeStartObject();
                        json.writeStringField("status", "OK");
                        json.writeObjectFieldStart("content");
                        // return format entity list
                        json.writeArrayFieldStart("format_entity");
                        for (Format format : formats.values()) {
                            json.writeStartObject();
                            new FormatSerializer(format).writeToJson(json);
                            json.writeEndObject();
                        }
                        json.writeEndArray();
                        json.writeEndObject();
                        json.writeEndObject();
                        return Response.ok().entity(utility.toUTF8String()).build();
                    } else if (formatPath != null) {
                        json.writeStartObject();
                        json.writeStringField("status", "OK");
                        json.writeObjectFieldStart("content");
                        json.writeArrayFieldStart("format_entity");
                        Format requestedFormat = requiredDs.getConnector().getFormat(formats.get(formatName), formatPath, direct);
                        requestedFormat = requestedFormat.getChildByPath(formatPath);
                        {
                            json.writeStartObject();
                            new FormatSerializer(requestedFormat).writeToJson(json);
                            json.writeEndObject();
                        }
                        json.writeEndArray();
                        json.writeEndObject();
                        json.writeEndObject();
                        return Response.ok().entity(utility.toUTF8String()).build();
                    }
                } catch (Exception ex) {
                    return ServerUtils.getErrorMessageResponse(ex);
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Not implemented").build();
    }

    private Response deleteProject(long projectID, HttpServletRequest req) throws Exception {
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        pDAO.delete(projectID);
        JsonUtility utility = JsonUtility.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeStringField("status", "OK");
        json.writeObjectFieldStart("result_content");
        json.writeArrayFieldStart("project");
        json.writeStartObject();
        json.writeNumberField("id", projectID);
        json.writeEndObject();
        json.writeEndArray();
        json.writeEndObject();
        json.writeEndObject();
        return Response.ok().entity(utility.toUTF8String()).build();
    }

    private Response deployProject(long projectID, HttpServletRequest req) throws Exception {
        this.getProject(projectID, req);
        return this.activeProject(projectID, req);
    }

    private Response getProjectTestExecResult(long projectID, long componentID) throws Exception {
        InOutLogDAO inoutLogDAO = DAOFactory.getInOutLogDAO();
        List<InOutLogItem> items = inoutLogDAO.getLogList(projectID, componentID);
        JsonUtility utility = JsonUtility.createJsonUtility();
        JsonGenerator json = utility.getGenerator();
        json.writeStartObject();
        json.writeStringField("status", "OK");
        json.writeObjectFieldStart("result_content");
        json.writeArrayFieldStart("log_items");
        for (InOutLogItem item : items) {
            json.writeStartObject();
            json.writeStringField("log_id", Long.toString(item.logID));
            json.writeStringField("component_id", Long.toString(item.componentID));
            json.writeStringField("target_name", item.targetName);
            json.writeStringField("direction", item.direction);
            json.writeStringField("log_data", item.data);
            json.writeStringField("last_modif_time", item.lastModifTime.toString());
            json.writeEndObject();
        }
        json.writeEndArray();
        json.writeEndObject();
        json.writeEndObject();
        return Response.ok().entity(utility.toUTF8String()).build();
    }

    private Response closeProject(long projectID, HttpServletRequest req) {
        Object attr = req.getSession().getAttribute(CURRENT__EDITING__PROJECT);
        if (attr != null) {
            try {
                Pair<String, TransformProject> pair = (Pair<String, TransformProject>) attr;
                pair.getSecond().close();
                JsonUtility utility = JsonUtility.createJsonUtility();
                JsonGenerator json = utility.getGenerator();
                json.writeStartObject();
                json.writeStringField("status", "OK");
                json.writeStringField("result_content", String.format("Project '%s' is closed", pair.getSecond().getName()));
                json.writeEndObject();
                req.getSession().removeAttribute(CURRENT__EDITING__PROJECT);
                return Response.ok().entity(utility.toUTF8String()).build();
            } catch (Exception ex) {
                return ServerUtils.getErrorMessageResponse(ex);
            }
        }
        return ServerUtils.getErrorMessageResponse("No project is opened!");
    }
}
