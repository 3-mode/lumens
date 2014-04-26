/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Lumens server entry point
 */
public class Application {

    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String WAR_PATH = System.getProperty("lumens.web", "module/web");
    private static Application application;
    private ApplicationContext context;
    public List<String> resultCache = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Application app = Application.createInstance(args);
        app.launch();
    }

    private static Application createInstance(String[] args) {
        application = new Application(new ApplicationContext(), args);
        return application;
    }

    private Application(ApplicationContext applicationContext, String[] args) {
        this.context = applicationContext;
    }

    private void launch() throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        ServletHolder jersey = new ServletHolder(new ServletContainer(new PackagesResourceConfig(new String[]{"com.lumens.server.service"})));
        jersey.setName("Jersey RESTful Service");
        ServletContextHandler restCtx = new ServletContextHandler();
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        restCtx.setHandler(sessions);
        restCtx.setContextPath("/");
        restCtx.addServlet(jersey, "/lumens/rest/*");
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.addHandler(restCtx);
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
        context.start();
        System.out.println("Starting server");
        server.start();
        server.join();
        context.stop();
    }

    public static Application get() {
        return application;
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

    public synchronized void cacheResultString(String result) {
        if (resultCache.size() < 20)
            resultCache.add(result);
        else if (resultCache.size() >= 20) {
            resultCache.set(resultCache.size() % 20, result);
        }
    }

    public synchronized String[] getCacheResultString() {
        String[] results = new String[resultCache.size()];
        return resultCache.toArray(results);
    }
}