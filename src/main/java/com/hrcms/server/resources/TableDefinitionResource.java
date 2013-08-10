package com.hrcms.server.resources;

import com.hrcms.server.resources.utils.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.commons.io.IOUtils;

@Path("tables")
public class TableDefinitionResource {
    @GET
    @Path("/{tableName}")
    @Produces("application/json")
    public String getTable(@PathParam("tableName") String tableName) throws IOException {
        InputStream in = null;
        try {
            in = TableDefinitionResource.class.getResourceAsStream("/tables/" + tableName + ".json");
            ByteArrayOutputStream baos = Utils.read(in);
            String json = baos.toString("UTF-8");
            System.out.println(json);
            return json;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
