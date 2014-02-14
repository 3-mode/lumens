/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import com.lumens.LumensException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class AddinEngine implements Addin {

    private AddinContext addinContext;
    private AddinURLClassLoader engineClassLoader;
    private List<AddinURLClassLoader> classLoaderList = new ArrayList<>();

    public AddinEngine() {
        engineClassLoader = new AddinURLClassLoader(new URL[]{}, ClassLoader.getSystemClassLoader());
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
