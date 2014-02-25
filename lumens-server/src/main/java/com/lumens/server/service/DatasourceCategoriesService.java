/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server.service;

import com.lumens.addin.ServiceEntity;
import com.lumens.connector.ConnectorFactory;
import com.lumens.server.Application;
import com.lumens.server.JsonUtility;
import com.lumens.server.ServerUtils;
import java.io.IOException;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/categories")
public class DatasourceCategoriesService {

    @GET
    @Path("/components")
    @Produces("application/json")
    public Response getComponentCategories() {
        try {
            JsonUtility utility = ServerUtils.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeArrayFieldStart("connectors");
            for (ServiceEntity service : Application.getInstance().getApplicationContext().getAddinEngine().getAddinContext().getServices()) {
                Map<String, Object> props = service.getPropertList();
                json.writeStartObject();
                json.writeStringField("id", props.get(ConnectorFactory.ID_PROPERTY).toString());
                json.writeStringField("name", props.get(ConnectorFactory.NAME_PROPERTY).toString());
                json.writeStringField("type", "datasource");
                json.writeStringField("icon", props.get(ConnectorFactory.CATALOG_ICON_PROPERTY).toString());
                json.writeStringField("instance_icon", props.get(ConnectorFactory.INSTANCE_ICON_PROPERTY).toString());
                json.writeEndObject();
            }
            json.writeEndArray();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception ex) {
            return ServerUtils.getErrorMessageResponse(ex);
        }
    }
}
