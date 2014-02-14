/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface AddinActivator {

    public void start(AddinContext ctx);

    public void stop(AddinContext ctx);
}