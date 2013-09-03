package com.hrcms.server.dao.factory;

import com.hrcms.server.dao.PersonSummaryDAO;
import com.hrcms.server.dao.PersonSummaryListDAO;
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
}
