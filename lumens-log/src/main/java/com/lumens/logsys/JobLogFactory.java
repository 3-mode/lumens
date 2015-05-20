/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.logsys;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class JobLogFactory implements LogFactory {
    private String lumensBase;
    private final String jobID;

    protected JobLogFactory(String lumensBase, long jobID) {
        this.lumensBase = lumensBase;
        this.jobID = Long.toString(jobID);
    }

    public static JobLogFactory create(String lumensBase, long jobID) {
        return new JobLogFactory(lumensBase, jobID);
    }

    @Override
    public void start() {
        if (lumensBase == null)
            lumensBase = "target";
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        Layout layout = PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN, config, null, null, true, false, null, null);
        TriggeringPolicy tp = SizeBasedTriggeringPolicy.createPolicy("10MB");
        Appender appender = RollingFileAppender.createAppender(String.format("%s/lumens-job-%s.log", lumensBase, jobID),
                                                               lumensBase + "/logs/$${date:yyyy-MM}/lumens-job-" + jobID + "-%d{MM-dd-yyyy}-%i.log.gz",
                                                               "true", jobID, null, null, null, tp, null, layout, null,
                                                               null, null, null, config);

        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef(jobID, null, null);
        AppenderRef[] refs = new AppenderRef[]{ref};
        LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.INFO, "org.apache.logging.log4j",
                                                              "true", refs, null, config, null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger(jobID, loggerConfig);
        ctx.updateLoggers();
    }

    @Override
    public void stop() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        config.removeLogger(jobID);
    }

    @Override
    public Logger getLogger() {
        return LogManager.getLogger(jobID);
    }

}
