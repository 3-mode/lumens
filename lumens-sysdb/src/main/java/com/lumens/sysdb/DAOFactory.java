package com.lumens.sysdb;

import com.lumens.sysdb.dao.InOutLogDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.dao.RelationDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class DAOFactory {

    private static final ApplicationContext context = new ClassPathXmlApplicationContext("system/datasource-config.xml");

    public static ProjectDAO getProjectDAO() {
        return (ProjectDAO) context.getBean("projectDAO");
    }
    public static JobDAO getJobDAO() {
        return (JobDAO) context.getBean("JobDAO");
    }
    public static RelationDAO getRelationDAO() {
        return (RelationDAO) context.getBean("RelationDAO");
    }    
    public static InOutLogDAO getInOutLogDAO() {
        return (InOutLogDAO) context.getBean("inputLogDAO");
    }
}
