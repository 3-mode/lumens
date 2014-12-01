/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformForeach {
    private final String sourcePath;
    private final String shortSourcePath;
    private final String indexName;
    private int indexValue;

    public TransformForeach(String sourcePath, String shortSourcePath, String indexName) {
        this(sourcePath, shortSourcePath, indexName, 0);
    }

    public TransformForeach(String sourcePath, String shortSourcePath, String indexName, int indexValue) {
        this.sourcePath = sourcePath;
        this.shortSourcePath = shortSourcePath;
        this.indexName = indexName;
        this.indexValue = indexValue;
    }

    public int getIndexValue() {
        return indexValue;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public boolean hasSourcePath() {
        return sourcePath != null && !sourcePath.isEmpty();
    }

    public String getShortSourcePath() {
        return shortSourcePath;
    }

    public String getIndexName() {
        return indexName;
    }

}
