package com.lumens.sysdb;

import com.lumens.sysdb.config.DatabaseAccessObject;
import com.lumens.sysdb.config.DatabaseAccessObjects;
import com.lumens.sysdb.config.Sql;
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

    private final Map<String, Sql> sqlList = new HashMap<>();

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
                    sqlList.put(obj.getName() + '/' + sql.getName(), sql);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public String getSQL(String sqlPath) {
        return sqlList.get(sqlPath).getvalue();
    }

    public String getSQL(String sqlPath, Object... arg) {
        return String.format(getSQL(sqlPath), arg);
    }
}
