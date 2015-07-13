import com.lumens.connector.database.DBUtils;

/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TestDbDriverInstance {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 200; ++i) {
            System.out.println("new db driver" + i);
            DBUtils.getInstance("file:///X:\\\\lumens\\\\dist\\\\3rdparty\\\\oracle\\\\jdbc\\\\ojdbc6.jar", "oracle.jdbc.OracleDriver");
        }
    }
}
