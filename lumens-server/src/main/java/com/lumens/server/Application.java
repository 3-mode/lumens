/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import java.io.File;
import java.nio.file.Paths;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.PropertiesConfigurationManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;

/**
 * Lumens server entry point
 */
public class Application {

    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String WAR_PATH = System.getProperty("lumens.web", "module/web");
    private static Application application;

    public static void main(String[] args) throws Exception {
        Application app = Application.createInstance(args);
        app.launch();
    }

    private static Application createInstance(String[] args) {
        application = new Application(args);
        return application;
    }

    private Application(String[] args) {
    }

    private void launch() throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{contexts, new DefaultHandler()});
        server.setHandler(handlers);

        // Configure Hot deployer
        DeploymentManager deployer = new DeploymentManager();
        deployer.setContexts(contexts);
        deployer.setContextAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/servlet-api-[^/]*\\.jar$");
        String monitorDir = new File(Paths.get(WAR_PATH).normalize().toString()).getAbsolutePath();
        String tempDir = new File(Paths.get(WAR_PATH + "/../runtime").normalize().toString()).getAbsolutePath();
        String defaultDesc = new File(Paths.get(WAR_PATH + "/../server/etc/webdefault.xml").normalize().toString()).getAbsolutePath();
        System.out.println("Monitor path [" + monitorDir + "]");
        System.out.println("Temp path [" + tempDir + "]");
        System.out.println("Default descriptor path [" + defaultDesc + "]");
        WebAppProvider webapp_provider = new WebAppProvider();
        webapp_provider.setMonitoredDirName(monitorDir);
        webapp_provider.setTempDir(new File(tempDir));
        webapp_provider.setScanInterval(1);
        webapp_provider.setExtractWars(false);
        webapp_provider.setDefaultsDescriptor(defaultDesc);
        webapp_provider.setConfigurationManager(new PropertiesConfigurationManager());
        deployer.addAppProvider(webapp_provider);
        server.addBean(deployer);

        // Configure the hot deployment
        System.out.println("Starting server");
        server.start();
        server.join();
    }

    public static Application get() {
        return application;
    }
}