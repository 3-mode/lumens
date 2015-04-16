/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.logsys;

import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class LogSysFactory {
    public static enum LOG_MODE {
        CONSOLE,
        FILE,
        JMS
    }

    private static LOG_MODE logMode;

    public static void setMode(LOG_MODE mode) {
        try {
            logMode = mode;
            if (mode == LOG_MODE.CONSOLE)
                System.setProperty("log4j.configurationFile", LogApplication.class.getResource("/conf/log4j2-console.xml").toURI().toString());
            else if (mode == LOG_MODE.JMS)
                System.setProperty("log4j.configurationFile", LogApplication.class.getResource("/conf/log4j2-jms.xml").toURI().toString());
            else
                System.setProperty("log4j.configurationFile", LogApplication.class.getResource("/conf/log4j2-file.xml").toURI().toString());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Logger getLogger(String name) {
        return LogManager.getLogger(name + " : LUMENS");
    }
}
