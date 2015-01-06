package com.lumens.backend.sql;

import com.lumens.backend.sql.dao.InOutLogDAO;
import com.lumens.backend.sql.dao.ProjectDAO;
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

    public static InOutLogDAO getInOutLogDAO() {
        return (InOutLogDAO) context.getBean("inputLogDAO");
    }
}
