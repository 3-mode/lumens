/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class MapperException extends RuntimeException {
    private final Element exElement;

    public MapperException(Exception e, Element elem) {
        super(e);
        exElement = elem;
    }

    public MapperException(String message) {
        super(message);
        exElement = null;
    }

    public Element getElementOfException() {
        return this.exElement;
    }
}
