/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class MyAddinActivator implements AddinActivator {

    private AddinContext context;

    public static class MyHello implements IHello {

        @Override
        public void sayHello(String name) {
            System.out.println("Hello " + name);
        }
    }

    @Override
    public void start(AddinContext ctx) {
        context = ctx;
        Map<String, Object> props = new HashMap<>();
        props.put("ServiceName", "SayHello");
        context.registerService(IHello.class.getName(), new MyHello(), props);
    }

    @Override
    public void stop(AddinContext ctx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
