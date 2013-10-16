package com.hrcms.server.dao.factory;

import com.hrcms.server.dao.AwardDAO;
import com.hrcms.server.dao.DictItemDAO;
import com.hrcms.server.dao.DictListDAO;
import com.hrcms.server.dao.EducationDAO;
import com.hrcms.server.dao.EducationInLandDAO;
import com.hrcms.server.dao.EducationOutLandDAO;
import com.hrcms.server.dao.EvaluationDAO;
import com.hrcms.server.dao.FamilyMemberDAO;
import com.hrcms.server.dao.PersonSummaryDAO;
import com.hrcms.server.dao.PersonSummaryListDAO;
import com.hrcms.server.dao.PunishmentDAO;
import com.hrcms.server.dao.ResumeItemDAO;
import com.hrcms.server.dao.TableColumnDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class HrcmsDAOFactory {
    private static ApplicationContext context = new ClassPathXmlApplicationContext("system/datasource-config.xml");

    public static PersonSummaryDAO getPersonSummaryDAO() {
        return (PersonSummaryDAO) context.getBean("personSummaryDAO");
    }

    public static PersonSummaryListDAO getPersonSummaryListDAO() {
        return (PersonSummaryListDAO) context.getBean("personSummaryListDAO");
    }

    public static DictListDAO getDictListDAO() {
        return (DictListDAO) context.getBean("dictListDAO");
    }

    public static DictItemDAO getDictItemDAO() {
        return (DictItemDAO) context.getBean("dictItemDAO");
    }

    public static TableColumnDAO getTableColumnDAO() {
        return (TableColumnDAO) context.getBean("tableColumnDAO");
    }

    public static EvaluationDAO getEvaluationDAO() {
        return (EvaluationDAO) context.getBean("evaluationDAO");
    }

    public static FamilyMemberDAO getFamilyMemberDAO() {
        return (FamilyMemberDAO) context.getBean("familyMemberDAO");
    }

    public static ResumeItemDAO getResumeItemDAO() {
        return (ResumeItemDAO) context.getBean("resumeItemDAO");
    }

    public static EducationDAO getEducationDAO() {
        return (EducationDAO) context.getBean("educationDAO");
    }

    public static EducationInLandDAO getEducationInLandDAO() {
        return (EducationInLandDAO) context.getBean("educationInLandDAO");
    }

    public static EducationOutLandDAO getEducationOutLandDAO() {
        return (EducationOutLandDAO) context.getBean("educationOutLandDAO");
    }

    public static PunishmentDAO getPunishmentDAO() {
        return (PunishmentDAO) context.getBean("punishmentDAO");
    }
    
    public static AwardDAO getAwardDAO() {
        return (AwardDAO) context.getBean("awardDAO");
    }
}
