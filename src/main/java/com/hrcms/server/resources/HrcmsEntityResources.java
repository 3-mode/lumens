package com.hrcms.server.resources;

import com.hrcms.server.dao.DictItemDAO;
import com.hrcms.server.dao.DictListDAO;
import com.hrcms.server.dao.PersonSummaryListDAO;
import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.dao.factory.HrcmsDAOFactory;
import com.hrcms.server.model.DictItem;
import com.hrcms.server.model.DictListRecord;
import com.hrcms.server.model.PersonSummaryListRecord;
import java.util.List;
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
        List<PersonSummaryListRecord> l = pDAO.getPersonSummaryRecordList();
        for (PersonSummaryListRecord ps : l) {
            if (sb.length() > 0) {
                sb.append(",\n");
            }
            sb.append(EntityFactory.createJsonFromEntity(ps));
        }
        sb.append("\n");

        return Response.ok().entity(String.format("{\n \"count\" : " + l.size() + ",\n \"person\":\n[ \n%s]\n}", sb.toString())).build();
    }

    @GET
    @Path("/person/{personId}")
    @Produces("application/json")
    public Response getPerson(@PathParam("personId") String personId) {
        return Response.ok().entity("{ \"test\": \"value\"}").build();
    }

    @GET
    @Path("/dictlist")
    @Produces("application/json")
    public Response getDictList() throws Exception {
        DictListDAO dDAO = HrcmsDAOFactory.getDictListDAO();
        StringBuilder sb = new StringBuilder();
        List<DictListRecord> l = dDAO.getDictTableList();
        for (DictListRecord t : l) {
            if (sb.length() > 0) {
                sb.append(",\n");
            }
            sb.append(EntityFactory.createJsonFromEntity(t));
        }

        return Response.ok().entity(String.format("[%s]", sb.toString())).build();
    }

    @GET
    @Path("/dict/{dictName}")
    @Produces("application/json")
    public Response getDictItemList(@PathParam("dictName") String dictName) throws Exception {
        DictItemDAO dDAO = HrcmsDAOFactory.getDictItemDAO();
        StringBuilder sb = new StringBuilder();
        List<DictItem> l = dDAO.getDictItemList(dictName);
        for (DictItem i : l) {
            if (sb.length() > 0) {
                sb.append(",\n");
            }
            sb.append(EntityFactory.createJsonFromEntity(i));
        }

        return Response.ok().entity(String.format("[%s]", sb.toString())).build();
    }
}
