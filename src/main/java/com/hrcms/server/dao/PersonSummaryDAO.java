package com.hrcms.server.dao;

import com.hrcms.server.model.PersonSummary;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
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
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from \"个人概况\"");
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();

            for (int i = 1; i <= count; ++i) {
                pList.add(new PersonSummary(i, rsmd.getColumnName(i)));
            }
        } finally {
            DbUtils.releaseResultSet(rs);
            DbUtils.releaseStatement(st);
            DbUtils.releaseConnection(con);
        }
        return pList;
    }
}
