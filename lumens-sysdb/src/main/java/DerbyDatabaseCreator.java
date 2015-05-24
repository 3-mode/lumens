
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DerbyDatabaseCreator {
    public static void main(String args[]) throws Exception {
        if (args.length == 3) {
            String dbDirectory = args[0];
            String dbUser = args[1];
            String dbPass = args[2];
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocol = "jdbc:derby:";
            Properties props = new Properties();
            props.setProperty("user", dbUser);
            props.setProperty("password", dbPass);
            props.setProperty("create", "true");
            Connection conn = DriverManager.getConnection(protocol + dbDirectory, props);
            Statement stat = conn.createStatement();
            try (InputStream in = DerbyDatabaseCreator.class.getResourceAsStream("sql/tables.sql")) {
                for (String sql : IOUtils.toString(in).split(";")) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        stat.execute(sql);
                    }
                }
                System.out.println("Build database succesfully !");
            }
        } else {
            System.out.println("Help:");
            System.out.println("Please input arguments: <db directory> <db user> <db password> in order");
            System.out.println("Example: C:/db/mydb sa 123");
        }
    }
}
