package com.hrcms.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class HrcmsServiceProvider extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp);
    }

    protected void doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String serviceURI = req.getRequestURI();
        if (serviceURI.endsWith("uuid")) {
            resp.setHeader("Content-Type", "application/json;charset=UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.write("{\"uuid\":\"" + UUID.randomUUID().toString() + "\"}");
            writer.flush();
        }
    }
}
