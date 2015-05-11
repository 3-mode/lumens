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

    private final Logger log = LogSysFactory.getLogger(LogSysUnitTest.class);

    static {
        LogSysFactory.setMode(LogSysFactory.LOG_MODE.FILE);
    }

    public LogSysUnitTest() {
        log.debug("in LogSysUnitTest 你好");
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testDebugConsole() {
        if (log.isDebugEnabled())
            log.debug("this is a unit testing info");
        assertTrue(true);
    }
}
