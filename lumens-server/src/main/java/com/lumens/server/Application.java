/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Lumens server entry point
 */
public class Application {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath("/lumens/ui");
        // TODO it should be configurable
        ctx.setWar("target/lib/lumens-web-1.0.war");
        ServletHolder jersey = new ServletHolder(new ServletContainer(new PackagesResourceConfig(new String[]{"com.lumens.server.service"})));
        jersey.setName("restful service servlet");
        ServletContextHandler restCtx = new ServletContextHandler();
        restCtx.setContextPath("/");
        restCtx.addServlet(jersey, "/lumens/rest/*");
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{ctx, restCtx, new DefaultHandler()});
        server.setHandler(handlers);
        server.start();
        server.join();
    }
}