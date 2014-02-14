
import com.lumens.connector.database.client.oracle.service.Activator;
import org.junit.Test;

/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class OracleConnectorTest {

    public OracleConnectorTest() {
    }

    @Test
    public void testEmpty() {
    }

    public void testActivator() {
        Activator a = new Activator();
        a.start(null);
    }
}