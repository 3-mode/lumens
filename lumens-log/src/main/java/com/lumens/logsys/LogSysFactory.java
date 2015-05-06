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
    public static void setMode(LOG_MODE mode) {
        try {
            if (mode == LOG_MODE.CONSOLE)
                System.setProperty("log4j.configurationFile", LogApplication.class.getResource("/conf/log4j2-console.xml").toURI().toString());
            else if (mode == LOG_MODE.JMS)
                System.setProperty("log4j.configurationFile", LogApplication.class.getResource("/conf/log4j2-jms.xml").toURI().toString());
            else if (mode == LOG_MODE.FILE)
                System.setProperty("log4j.configurationFile", LogApplication.class.getResource("/conf/log4j2-file.xml").toURI().toString());
            else
                throw new RuntimeException("Unsupported log mode !");
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void checkConfigureFile() {
        try {
            String mode = System.getProperty("log4j.configurationFile");
            if (mode == null || mode.isEmpty())
                System.setProperty("log4j.configurationFile", LogApplication.class.getResource("/conf/log4j2-console.xml").toURI().toString());
        } catch (URISyntaxException ex) {
        }
    }

    public static Logger getLogger(String name) {
        checkConfigureFile();
        return LogManager.getLogger("LUMENS" + " : " + name);
    }

    public static Logger getLogger(Class<?> clazz) {
        checkConfigureFile();
        return LogManager.getLogger("LUMENS" + " : " + clazz.getName());
    }
}
