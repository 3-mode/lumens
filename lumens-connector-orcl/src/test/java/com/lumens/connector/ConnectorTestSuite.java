package com.lumens.connector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class ConnectorTestSuite extends TestCase {

    public ConnectorTestSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("ConnectorTestSuite");
        suite.addTest(ConnectorTest.suite());
        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
