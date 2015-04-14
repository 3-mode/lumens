/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.logsys;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
class LogSysInitialContextFactory implements InitialContextFactory {

    @SuppressWarnings("UseOfObsoleteCollectionType")
    private final InitialContext initialCtx;

    public LogSysInitialContextFactory(InitialContext initialCtx) throws NamingException {
        this.initialCtx = initialCtx;
    }

    @Override
    public Context getInitialContext(@SuppressWarnings("UseOfObsoleteCollectionType") Hashtable<?, ?> environment) throws NamingException {
        return initialCtx;
    }
}
