/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server.service;

import com.lumens.addin.ServiceEntity;
import com.lumens.descriptor.DescriptorUtils;
import com.lumens.io.JsonUtility;
import com.lumens.server.Application;
import com.lumens.server.ServerUtils;
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
public class DatasourceCategoryService {

    @GET
    @Path("/components")
    @Produces("application/json")
    public Response getComponentCategories() {
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeArrayFieldStart("items");
            for (ServiceEntity service : Application.get().getApplicationContext().getTransformEngine().getAddinEngine().getAddinContext().getServices()) {
                Map<String, Object> props = service.getPropertList();
                json.writeObject(props.get(DescriptorUtils.DESCRIPTOR));
            }
            json.writeEndArray();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception ex) {
            return ServerUtils.getErrorMessageResponse(ex);
        }
    }
}
