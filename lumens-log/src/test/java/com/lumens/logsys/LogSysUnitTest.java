/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.logsys;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class LogSysUnitTest {

    private final Logger log = SysLogFactory.getLogger(LogSysUnitTest.class);

    static {
        SysLogFactory.start("console", LogSysUnitTest.class.getResource("/").getPath() + "../classes");
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testDebugConsole() {
        if (log.isDebugEnabled()) {
            log.debug("this is a unit testing info");
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void testNewLogger() {
        JobLogFactory factory = JobLogFactory.create(null, 1000);
        factory.start();
        factory.getLogger().info("Testing testing testing 2");
        factory.stop();
    }
}
