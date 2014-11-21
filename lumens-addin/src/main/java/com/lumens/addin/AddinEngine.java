/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import com.lumens.LumensException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class AddinEngine implements Addin {

    private final AddinContext addinContext;
    private final AddinURLClassLoader engineClassLoader;
    private List<AddinURLClassLoader> classLoaderList = new ArrayList<>();
    
    public AddinEngine(ClassLoader parentClassLoader) {
        engineClassLoader = new AddinURLClassLoader(new URL[]{}, parentClassLoader == null ? ClassLoader.getSystemClassLoader() : parentClassLoader);
        addinContext = new AddinDefaultContext(this);
    }

    public AddinURLClassLoader getEngineClassLoader() {
        return engineClassLoader;
    }

    public AddinEngine loadSystemClass(URL classPath) {
        try {
            File systemJarPath = new File(classPath.toURI());
            for (File systeJar : systemJarPath.listFiles(AddinURLClassLoader.lumensJarFilter)) {
                getEngineClassLoader().addJarLocationOrPathLoactionURL(systeJar.toURI().toURL());
            }
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new LumensException(ex);
        }
        return this;
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
