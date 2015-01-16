/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
import static com.lumens.connector.database.DBConstants.ACTION;
import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import static com.lumens.connector.database.sqlserver.SQLServerConstants.SQLSERVER_PK;
import com.lumens.connector.database.sqlserver.SQLServerQuerySQLBuilder;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class SQLServerTest {

    public SQLServerTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testSelectSQL() throws Exception {
        Format employeeFmtTest = new DataFormat("itam.amComputer", Format.Form.STRUCT);
        Format sqlParamsTest = employeeFmtTest.addChild(SQLPARAMS, Format.Form.STRUCT);
        sqlParamsTest.addChild(ACTION, Format.Form.FIELD, Type.STRING);
        employeeFmtTest.addChild(new DataFormat("lComputerId", Format.Form.FIELD, Type.INTEGER));
        employeeFmtTest.addChild(new DataFormat("AssetTag", Format.Form.FIELD, Type.STRING));
        employeeFmtTest.addChild(new DataFormat("TcpIpHostName", Format.Form.FIELD, Type.STRING));
        employeeFmtTest.setProperty(SQLSERVER_PK, new Value("lComputerId"));
        Element select = new DataElement(employeeFmtTest);
        select.addChild(SQLPARAMS).addChild(ACTION).setValue("SELECT");
        SQLServerQuerySQLBuilder sqlTest = new SQLServerQuerySQLBuilder(employeeFmtTest);
        String sqlSelect = sqlTest.generateSelectSQL(select);
        System.out.println("Generated select SQL: " + sqlTest.generatePageSQL(sqlSelect, 1, 100));
    }
}
