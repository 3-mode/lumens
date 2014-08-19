/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface Addin {

    public ServiceEntity getService(String serviceIdentifier);

    public void start();

    public void stop();

    public String getName();

    public Addin getAddin(String name);
}
