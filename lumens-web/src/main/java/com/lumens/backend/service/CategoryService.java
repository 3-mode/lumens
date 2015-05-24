/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.addin.ServiceEntity;
import com.lumens.backend.ApplicationContext;
import com.lumens.descriptor.DescriptorUtils;
import com.lumens.io.JsonUtility;
import com.lumens.backend.ServerUtils;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/category")
public class CategoryService {

    @GET
    @Path("/component")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComponentCategories() {
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeArrayFieldStart("items");
            for (ServiceEntity service : ApplicationContext.get().getTransformEngine().getAddinEngine().getAddinContext().getServices()) {
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
