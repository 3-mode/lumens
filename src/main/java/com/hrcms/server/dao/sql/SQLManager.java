package com.hrcms.server.dao.sql;

import com.hrcms.server.dao.sql.config.DatabaseAccessObject;
import com.hrcms.server.dao.sql.config.DatabaseAccessObjects;
import com.hrcms.server.dao.sql.config.Sql;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class SQLManager {
    private Map<String, Sql> sqlHolder = new HashMap<String, Sql>();

    public SQLManager(String strSQLURL) {
        loadSQL(strSQLURL);
    }

    private void loadSQL(String strSQLURL) throws RuntimeException {
        InputStream in = null;
        try {
            in = SQLManager.class.getClassLoader().getResourceAsStream(strSQLURL);
            JAXBContext context = JAXBContext.newInstance(DatabaseAccessObjects.class);
            Unmarshaller um = context.createUnmarshaller();
            DatabaseAccessObjects objects = (DatabaseAccessObjects) um.unmarshal(in);
            for (DatabaseAccessObject obj : objects.getDatabaseAccessObject()) {
                for (Sql sql : obj.getSql()) {
                    sql.setvalue(sql.getvalue().trim());
                    sqlHolder.put(obj.getName() + '/' + sql.getName(), sql);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public String getSQL(String sqlPath) {
        return sqlHolder.get(sqlPath).getvalue();
    }
}
