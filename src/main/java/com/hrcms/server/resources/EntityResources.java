package com.hrcms.server.resources;

import com.hrcms.server.dao.AwardDAO;
import com.hrcms.server.dao.DictItemDAO;
import com.hrcms.server.dao.DictListDAO;
import com.hrcms.server.dao.EducationDAO;
import com.hrcms.server.dao.EducationInLandDAO;
import com.hrcms.server.dao.EducationOutLandDAO;
import com.hrcms.server.dao.EvaluationDAO;
import com.hrcms.server.dao.FamilyMemberDAO;
import com.hrcms.server.dao.PersonSummaryListDAO;
import com.hrcms.server.dao.PunishmentDAO;
import com.hrcms.server.dao.ResumeItemDAO;
import com.hrcms.server.dao.TableColumnDAO;
import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.dao.factory.HrcmsDAOFactory;
import com.hrcms.server.model.AwardItem;
import com.hrcms.server.model.DictItem;
import com.hrcms.server.model.DictListRecord;
import com.hrcms.server.model.EducationInLandItem;
import com.hrcms.server.model.EducationItem;
import com.hrcms.server.model.EducationOutLandItem;
import com.hrcms.server.model.Evaluation;
import com.hrcms.server.model.FamilyMember;
import com.hrcms.server.model.PersonSummaryListRecord;
import com.hrcms.server.model.Punishment;
import com.hrcms.server.model.ResumeItem;
import com.hrcms.server.model.TableColumn;
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
public class EntityResources {
    public static final String columnTemplate = "{ \"columns\": [\n%s\n]}";

    @GET
    @Path("/person")
    @Produces("application/json")
    public Response getPersons(@QueryParam("filter") String filter) throws Exception {
        PersonSummaryListDAO pDAO = HrcmsDAOFactory.getPersonSummaryListDAO();
        List<PersonSummaryListRecord> l = pDAO.getPersonSummaryRecordList();
        return Response.ok().entity(String.format("{\n \"count\" : " + l.size() + ",\n \"person\":\n[ \n%s]\n}", convertToJson(l))).build();
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
        List<DictListRecord> l = dDAO.getDictTableList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/dictcolumns/{dictName}")
    @Produces("application/json")
    public Response getDictColumnList(@PathParam("dictName") String dictName) throws Exception {
        TableColumnDAO dDAO = HrcmsDAOFactory.getTableColumnDAO();
        StringBuilder sb = new StringBuilder();
        List<TableColumn> l = dDAO.getColumnList(dictName);
        for (TableColumn c : l) {
            if (sb.length() > 0) {
                sb.append(",\n");
            }
            sb.append(String.format("{\"label\":\"%s\", \"field\":\"%s\"}", c.columnName, c.columnName));
        }

        return Response.ok().entity(String.format(columnTemplate, sb.toString())).build();
    }

    @GET
    @Path("/dict/{dictName}")
    @Produces("application/json")
    public Response getDictItemList(@PathParam("dictName") String dictName) throws Exception {
        DictItemDAO dDAO = HrcmsDAOFactory.getDictItemDAO();
        List<DictItem> l = dDAO.getDictItemList(dictName);
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/evaluation")
    @Produces("application/json")
    public Response getEvaluation() throws Exception {
        EvaluationDAO eDAO = HrcmsDAOFactory.getEvaluationDAO();
        List<Evaluation> l = eDAO.getEvaluationItemList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/family")
    @Produces("application/json")
    public Response getFamilyMemberList() throws Exception {
        FamilyMemberDAO fDAO = HrcmsDAOFactory.getFamilyMemberDAO();
        List<FamilyMember> l = fDAO.getFamilyMemberList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/resume")
    @Produces("application/json")
    public Response getResumeItemList() throws Exception {
        ResumeItemDAO rDAO = HrcmsDAOFactory.getResumeItemDAO();
        List<ResumeItem> l = rDAO.getResumeItemList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/education")
    @Produces("application/json")
    public Response getEducationItemList() throws Exception {
        EducationDAO eDAO = HrcmsDAOFactory.getEducationDAO();
        List<EducationItem> l = eDAO.getEducationItemList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/educationinland")
    @Produces("application/json")
    public Response getEducationInLandItemList() throws Exception {
        EducationInLandDAO eDAO = HrcmsDAOFactory.getEducationInLandDAO();
        List<EducationInLandItem> l = eDAO.getEducationInLandItemList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/educationoutland")
    @Produces("application/json")
    public Response getEducationOutLandItemList() throws Exception {
        EducationOutLandDAO eDAO = HrcmsDAOFactory.getEducationOutLandDAO();
        List<EducationOutLandItem> l = eDAO.getEducationOutLandItemList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/award")
    @Produces("application/json")
    public Response getAwardList() throws Exception {
        AwardDAO aDAO = HrcmsDAOFactory.getAwardDAO();
        List<AwardItem> l = aDAO.getAwardItemList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    @GET
    @Path("/punishment")
    @Produces("application/json")
    public Response getPunishmentList() throws Exception {
        PunishmentDAO pDAO = HrcmsDAOFactory.getPunishmentDAO();
        List<Punishment> l = pDAO.getPunishmentList();
        return Response.ok().entity(String.format("[%s]", convertToJson(l))).build();
    }

    private static <T> String convertToJson(List<T> l) throws Exception {
        StringBuilder b = new StringBuilder();
        for (T t : l) {
            if (b.length() > 0) {
                b.append(",\n");
            }
            b.append(EntityFactory.createJsonFromEntity(t));
        }
        return b.toString();
    }
}
