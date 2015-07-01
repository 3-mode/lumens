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

    private final String lang;
    private AddinEngine addinEngine;
    private ClassLoader addinClassLoader;

    public TransformEngine() {
        this("en_US", TransformEngine.class.getClassLoader());
    }

    public TransformEngine(String lang, ClassLoader addinClassLoader) {
        this.lang = lang;
        this.addinClassLoader = addinClassLoader;
    }

    public void execute(Executor executor) throws Exception {
        executor.execute();
    }

    public void start(String addinPath) {
        try {
            AddinContext ac = null;
            if (addinPath != null && !addinPath.isEmpty()) {
                addinEngine = new AddinEngine(lang, addinClassLoader);
                addinEngine.start();
                ac = addinEngine.getAddinContext();
                System.out.println("Addin path: " + addinPath);
                File addinPathFile = new File(addinPath);
                if (addinPathFile.exists()) {
                    for (File addinItemFile : addinPathFile.listFiles()) {
                        ac.installAddIn(addinItemFile.toURI().toURL()).start();
                    }
                } else {
                    throw new RuntimeException(String.format("Wrong addin path '%s', it doesn't exist!", addinPath));
                }
            }
            TransformEngineContext.start(new DataSourceConnectorFactoryManager(ac));
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
