package com.hrcms.server.dao.sql;

import com.hrcms.server.dao.sql.config.DatabaseAccessObjects;
import com.hrcms.server.dao.sql.config.Sql;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class SQLManager {
    class DatabaseAccessObjectHolder {
        Map<String, Sql> sqlHolder = new HashMap<String, Sql>();
    }

    class DatabaseAccessObjectsHolder {
        Map<String, DatabaseAccessObjectHolder> daoHolder = new HashMap<String, DatabaseAccessObjectHolder>();
    }

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
            System.out.println("dao object list size: " + objects.getDatabaseAccessObject().size());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
