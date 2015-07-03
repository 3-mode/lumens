/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.desc.DescLoader;
import java.util.Map;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ConnectorActivator implements AddinActivator {

    protected AddinContext addinContext;
    private final ConnectorFactory factory;
    private final Class clazz;

    public ConnectorActivator(ConnectorFactory factory, Class clazz) {
        this.factory = factory;
        this.clazz = clazz;
    }

    @Override
    public void start(AddinContext context) {
        addinContext = context;
        Map<String, Object> props = DescLoader.load(context.getLanguage(), factory.getComponentType(), clazz);
        addinContext.registerService(factory.getComponentType(), factory, props);
    }

    @Override
    public void stop(AddinContext context) {
    }
}
