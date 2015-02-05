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
            DBUtils.getInstance("file:///C:\\oraclexe\\app\\oracle\\product\\11.2.0\\server\\jdbc\\lib\\ojdbc6.jar", "oracle.jdbc.OracleDriver");
        }
    }
}
