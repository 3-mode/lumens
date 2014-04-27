/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.container.servlet.WebConfig;
import javax.servlet.ServletException;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class LumensServletContainer extends ServletContainer {

    @Override
    protected void init(WebConfig webConfig) throws ServletException {
        super.init(webConfig);
        ApplicationContext.createInstance(this.getServletContext().getClassLoader());
        System.out.println("Addin loaded");
    }
}