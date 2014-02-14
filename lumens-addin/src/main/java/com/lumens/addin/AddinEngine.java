/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class AddinEngine implements Addin {

    private AddinContext addinContext;
    private AddinURLClassLoader engineClassLoader;
    private List<AddinURLClassLoader> classLoaderList = new ArrayList<>();

    public AddinEngine() {
        engineClassLoader = new AddinURLClassLoader(new URL[]{}, getClass().getClassLoader());
        addinContext = new AddinDefaultContext(this);
    }

    public AddinURLClassLoader getEngineClassLoader() {
        return engineClassLoader;
    }

    public AddinEngine addClassLoader(AddinURLClassLoader classLoader) {
        classLoaderList.add(classLoader);
        return this;
    }

    public AddinURLClassLoader newDefaultAddinClassLoader() {
        AddinURLClassLoader cl = new AddinURLClassLoader(new URL[]{}, this.getEngineClassLoader());
        classLoaderList.add(cl);
        return cl;
    }

    public List<AddinURLClassLoader> getClassLoaderList() {
        return classLoaderList;
    }

    @Override
    public void start() {
        addinContext.start();
    }

    @Override
    public void stop() {
        addinContext.stop();
        classLoaderList = new ArrayList<>();
    }

    public AddinContext getAddinContext() {
        return addinContext;
    }

    @Override
    public ServiceEntity getService(String serviceIdentifier) {
        return addinContext.getService(serviceIdentifier);
    }

    @Override
    public String getName() {
        return "AddinEngine";
    }

    @Override
    public Addin getAddin(String name) {
        return addinContext.getAddin(name);
    }
}
