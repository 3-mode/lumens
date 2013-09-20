package com.hrcms.server.dao;

import com.hrcms.server.model.PersonSummary;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.jdbc.pool.DataSource;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class PersonSummaryDAO {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<PersonSummary> getPersonSummaryList() throws Exception {
        List<PersonSummary> pList = new ArrayList<PersonSummary>();
        return pList;
    }
}
