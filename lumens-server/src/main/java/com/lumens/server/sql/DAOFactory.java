package com.lumens.server.sql;

import com.lumens.server.sql.dao.ProjectDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class DAOFactory {

    private static ApplicationContext context = new ClassPathXmlApplicationContext("system/datasource-config.xml");

    public static ProjectDAO getProjectDAO() {
        return (ProjectDAO) context.getBean("ProjectDAO");
    }
}
