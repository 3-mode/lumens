package com.hrcms.server.resources;

import com.hrcms.server.dao.PersonSummary;
import com.hrcms.server.dao.PersonSummaryDAO;
import com.hrcms.server.dao.factory.HrcmsDAOFactory;
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
        PersonSummaryDAO pDAO = HrcmsDAOFactory.getPersonSummaryDAO();
        StringBuilder sb = new StringBuilder();
        for (PersonSummary ps : pDAO.getPersonSummaryList()) {
            sb.append("\"id\" : \"").append(ps.getEmployeeID()).append("\",").append("\"name\" : \"").append(ps.getEmployeeName()).append("\"");
        }

        return Response.ok().entity(String.format("{ \"person\": { %s } }", sb.toString())).build();
    }

    @GET
    @Path("/person/{personId}")
    @Produces("application/json")
    public Response getPerson(@PathParam("personId") String personId) {
        return Response.ok().entity("{ \"test\": \"value\"}").build();
    }
}
