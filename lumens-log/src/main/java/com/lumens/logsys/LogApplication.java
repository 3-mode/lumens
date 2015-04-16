/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.logsys;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class LogApplication {
    private final Logger logger = LogSysFactory.getLogger(LogApplication.class.getName());
    private final Logger fileLogger = LogSysFactory.getLogger("System");

    public void start() throws NamingException {
        fileLogger.info("Start to log into JMS");
        logger.trace("Start Log Application");
        logger.debug("Start Log Application");
        logger.info("Start Log Application");
        logger.warn("Start Log Application");
        logger.error("Start Log Application");
        for (int i = 0; i < 100; ++i) {
            new Test1("hello Test" + i);
        }
        fileLogger.info("Finish logging into JMS");
    }

    public static void main(String args[]) throws NamingException, URISyntaxException, IOException {
        Properties props = new Properties();
        try (InputStream in = LogApplication.class.getResourceAsStream("/conf/jndi.properties")) {
            props.load(in);
        }
        NamingManager.setInitialContextFactoryBuilder(new LogSysInitialContextFactoryBuilder(props));
        LogSysFactory.setMode(LogSysFactory.LOG_MODE.CONSOLE);
        LogApplication app = new LogApplication();
        app.start();
        //System.exit(0);
        //ctx.close();
    }
}

class Test1 {
    private final Logger logger = LogManager.getLogger(Test1.class.getName());

    public Test1(String message) {
        logger.info(message);
    }

}
