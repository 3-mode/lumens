/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.lumens.LumensException;
import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.engine.EngineContext;
import com.lumens.engine.TransformEngine;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * Hold all the application information
 */
public class ApplicationContext {

    private static String ADDIN_PATH = System.getProperty("lumens.addin", "addin");
    private TransformEngine engine;
    private ProjectContext projectContext;
    private String[] bundleLocations;
    private AddinEngine addinEngine;

    public ApplicationContext() {
        engine = new TransformEngine();
        projectContext = new ProjectContext();
    }

    public ProjectContext getProjectContext() {
        return projectContext;
    }

    public ApplicationContext setProjectContext(ProjectContext projectContext) {
        this.projectContext = projectContext;
        return this;
    }

    public ApplicationContext setTransformEngine(TransformEngine engine) {
        this.engine = engine;
        return this;
    }

    public TransformEngine getTransformEngine() {
        return this.engine;
    }

    public AddinEngine getAddinEngine() {
        return this.addinEngine;
    }

    public void init() {
        try {
            addinEngine = new AddinEngine();
            addinEngine.start();
            AddinContext ac = addinEngine.getAddinContext();
            File addinPathFile = new File(ADDIN_PATH);
            for (File addinItemFile : addinPathFile.listFiles())
                ac.installAddIn(addinItemFile.toURI().toURL()).start();
            EngineContext.init(new ConnectorFactoryHolderImpl(ac));
            // TODO
            System.out.println("Loaded addin: " + Arrays.toString(ac.getAddins().toArray()));
        } catch (MalformedURLException ex) {
            throw new LumensException(ex);
        }
    }

    public void clean() {
    }
}
