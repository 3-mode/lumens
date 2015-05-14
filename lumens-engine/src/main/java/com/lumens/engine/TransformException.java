/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformException extends RuntimeException {
    private final TransformComponent transformComponent;

    public TransformException(TransformComponent tc, Exception ex) {
        super(ex);
        this.transformComponent = tc;
    }

    public TransformComponent getComponentOfException() {
        return this.transformComponent;
    }
}
