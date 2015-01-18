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
    private int start;

    public ElementChunk(List<Element> data) {
        this(true, data, 0);
    }

    public ElementChunk(boolean isLast, List<Element> data) {
        this(isLast, data, 0);
    }

    public ElementChunk(List<Element> data, int start) {
        this(true, data, start);
    }

    public ElementChunk(boolean isLast, List<Element> data, int start) {
        this.isLast = isLast;
        this.data = data;
        this.start = start;
    }

    /**
     * @return the isLast
     */
    public boolean isLast() {
        return isLast;
    }

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the data
     */
    public List<Element> getData() {
        return data;
    }
}
