package com.hrcms.server.resources;

import com.hrcms.server.dao.PersonSummaryListDAO;
import com.hrcms.server.dao.factory.HrcmsDAOFactory;
import com.hrcms.server.model.PersonSummaryListRecord;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
@Path("entities")
public class HrcmsEntityResources {
    @GET
    @Path("/person")
    @Produces("application/json")
    public Response getPersons(@QueryParam("filter") String filter) throws Exception {
        PersonSummaryListDAO pDAO = HrcmsDAOFactory.getPersonSummaryListDAO();
        StringBuilder sb = new StringBuilder();
        for (PersonSummaryListRecord ps : pDAO.getPersonSummaryRecordList()) {
            if(sb.length() > 0)
                sb.append(",\n");
            sb.append("{ \"id\" : \"").append(ps.getEmployeeID()).append("\",").append("\"name\" : \"").append(ps.getEmployeeName()).append("\" }");
        }
        sb.append("\n");

        return Response.ok().entity(String.format("{ \"person\":\n[ \n%s]\n}", sb.toString())).build();
    }

    @GET
    @Path("/person/{personId}")
    @Produces("application/json")
    public Response getPerson(@PathParam("personId") String personId) {
        return Response.ok().entity("{ \"test\": \"value\"}").build();
    }
}
