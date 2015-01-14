/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.addin.ServiceEntity;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.database.client.oracle.service.Activator;
import java.util.Arrays;
import org.junit.Test;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class OracleTest {

    public OracleTest() {
    }

    @Test
    public void testAddin() throws Exception {
        AddinEngine ae = new AddinEngine(OracleTest.class.getClassLoader());
        ae.start();
        AddinContext ac = ae.getAddinContext();
        Activator activator = new Activator();
        activator.start(ac);
        ServiceEntity<ConnectorFactory> se = ac.getService("type-oracle-jdbc");
        System.out.println(Arrays.toString(se.getPropertList().entrySet().toArray()));
    }
}
