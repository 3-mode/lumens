/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.LumensException;
import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.engine.DefaultConnectorFactoryHolder;
import com.lumens.engine.EngineContext;
import java.io.File;
import java.net.MalformedURLException;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ApplicationAddinManager {
    private final ClassLoader addinClassLoader;
    private AddinEngine addinEngine;

    public ApplicationAddinManager(ClassLoader addinClassLoader) {
        this.addinClassLoader = addinClassLoader;
    }

    public void start(String addinPath) {
        try {
            addinEngine = new AddinEngine(addinClassLoader);
            addinEngine.start();
            AddinContext ac = addinEngine.getAddinContext();
            System.out.println("Addin path: " + addinPath);
            File addinPathFile = new File(addinPath);
            for (File addinItemFile : addinPathFile.listFiles()) {
                ac.installAddIn(addinItemFile.toURI().toURL()).start();
            }
        } catch (MalformedURLException ex) {
            throw new LumensException(ex);
        }
    }

    public AddinEngine getAddinEngine() {
        return this.addinEngine;
    }
}
