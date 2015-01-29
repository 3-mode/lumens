/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.LumensException;
import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.engine.run.Executor;
import java.io.File;
import java.net.MalformedURLException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformEngine {

    private AddinEngine addinEngine;
    private ClassLoader addinClassLoader;

    public TransformEngine() {
        this(TransformEngine.class.getClassLoader());
    }

    public TransformEngine(ClassLoader addinClassLoader) {
        this.addinClassLoader = addinClassLoader;
    }

    public void execute(Executor executor) throws Exception {
        executor.execute();
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
            TransformEngineContext.start(new DefaultConnectorFactoryHolder(ac));
        } catch (MalformedURLException ex) {
            throw new LumensException(ex);
        }
    }

    public void start(AddinEngine addinEngine) {
        this.addinEngine = addinEngine;
    }

    public AddinEngine getAddinEngine() {
        return this.addinEngine;
    }
}
