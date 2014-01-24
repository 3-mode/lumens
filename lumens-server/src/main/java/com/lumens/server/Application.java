/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Lumens server entry point
 */
public class Application {

    public List<String> resultCache = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Application app = Application.createInstance();
        app.launch();
    }
    private static Application application;
    private ApplicationContext context;

    private static Application createInstance() {
        application = new Application(new ApplicationContext());
        return application;
    }

    private Application(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    private void launch() throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath("/lumens/ui");
        // TODO it should be configurable
        ctx.setWar("target/lib/lumens-web-1.0.war");
        ServletHolder jersey = new ServletHolder(new ServletContainer(new PackagesResourceConfig(new String[]{"com.lumens.server.service"})));
        jersey.setName("Jersey RESTful Service");
        ServletContextHandler restCtx = new ServletContextHandler();
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        restCtx.setHandler(sessions);
        restCtx.setContextPath("/");
        restCtx.addServlet(jersey, "/lumens/rest/*");
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{ctx, restCtx, new DefaultHandler()});
        server.setHandler(handlers);
        server.start();
        server.join();
    }

    public static Application getInstance() {
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