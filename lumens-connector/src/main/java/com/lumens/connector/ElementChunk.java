/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ElementChunk {
    private final boolean isLast;
    private final List<Element> data;

    public ElementChunk(List<Element> data) {
        this(true, data);
    }

    public ElementChunk(boolean isLast, List<Element> data) {
        this.isLast = isLast;
        this.data = data;
    }

    /**
     * @return the isLast
     */
    public boolean isLast() {
        return isLast;
    }

    /**
     * @return the data
     */
    public List<Element> getData() {
        return data;
    }
}
