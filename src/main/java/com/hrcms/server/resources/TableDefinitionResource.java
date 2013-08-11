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
    // Utility method
    private String getTableJson(String subPath, String tableName) throws IOException {
        InputStream in = null;
        try {
            in = TableDefinitionResource.class.getResourceAsStream("/tables/" + tableName + ".json");
            ByteArrayOutputStream baos = Utils.read(in);
            String json = baos.toString("UTF-8");
            return json;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @GET
    @Path("/base/{tableName}")
    @Produces("application/json")
    public String getBaseTable(@PathParam("tableName") String tableName) throws IOException {
        return getTableJson("base", tableName);
    }

    @GET
    @Path("/basic/{tableName}")
    @Produces("application/json")
    public String getBasicTable(@PathParam("tableName") String tableName) throws IOException {
        return getTableJson("basic", tableName);
    }

    @GET
    @Path("/resume/{tableName}")
    @Produces("application/json")
    public String getResumeTable(@PathParam("tableName") String tableName) throws IOException {
        return getTableJson("resume", tableName);
    }

    @GET
    @Path("/job/{tableName}")
    @Produces("application/json")
    public String getJobTable(@PathParam("tableName") String tableName) throws IOException {
        return getTableJson("job", tableName);
    }
}
