/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface AddinContext {

    public <T> T registerService(String componentType, T service, Map<String, Object> props);

    public ServiceEntity getService(String componentType);

    public Addin installAddIn(URL url);

    public Addin getAddin(String name);

    public List<Addin> getAddins();

    public List<ServiceEntity> getServices();

    public void start();

    public void stop();
}
