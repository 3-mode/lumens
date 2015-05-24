/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.logsys;

import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class LogSysInitialContextFactoryBuilder implements InitialContextFactoryBuilder {
    private final InitialContext initialCtx;

    public LogSysInitialContextFactoryBuilder(Properties props) throws NamingException {
        initialCtx = new InitialContext(props);
    }

    @Override
    public InitialContextFactory createInitialContextFactory(@SuppressWarnings("UseOfObsoleteCollectionType") java.util.Hashtable<?, ?> environment) throws NamingException {
        return new LogSysInitialContextFactory(initialCtx);
    }

}
