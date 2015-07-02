/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import java.io.File;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */

public class AddinApplicationTest {

     // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}

    public void testAddinEngine() throws Exception {
        AddinEngine ae = new AddinEngine("en_US", AddinApplicationTest.class.getClassLoader()).loadSystemClass(new File("../dist/lumens/module/server/lib").toURI().toURL());
        ae.start();
        AddinContext ac = ae.getAddinContext();
        Addin addin = ac.installAddIn(new File("../dist/lumens/addin/orcl").toURI().toURL());
        addin.start();
        System.out.println("Loaded addin: " + addin.getName());
        ServiceEntity<ConnectorFactory> se = addin.getService("type-oracle-jdbc");
        Connector c = se.getService().createConnector();
        assertNotNull(c);
        System.out.println("Created class instance: " + c);
        System.out.println("Properties: " + Arrays.toString(se.getPropertList().entrySet().toArray()));
        ae.stop();
    }

    @Test
    public void testAddinActivator() throws Exception {
        AddinEngine ae = new AddinEngine("en_US", AddinApplicationTest.class.getClassLoader());
        ae.start();
        AddinContext ac = ae.getAddinContext();
        AddinActivator aa = new MyAddinActivator();
        aa.start(ac);
        // Test addin
        ServiceEntity<IHello> se = ac.getService(IHello.class.getName());
        se.getService().sayHello("James");

        // Clean all
        ae.stop();
    }
}
