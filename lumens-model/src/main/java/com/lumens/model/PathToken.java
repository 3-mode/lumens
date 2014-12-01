/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author shaofeng wang
 */
public class PathToken {

    private String token;
    private int index;
    private String indexExpression;

    public PathToken(String token) {
        this.token = token;
        parseToken(token);
    }

    public boolean isIndexed() {
        return indexExpression != null && !indexExpression.isEmpty();
    }

    public boolean isIndexExpression() {
        return index < 0 && indexExpression != null && !indexExpression.isEmpty();
    }

    public String indexExpression() {
        return this.indexExpression;
    }

    public int index() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return token;
    }

    private void parseToken(String token) {
        if (token.charAt(token.length() - 1) != ']' || token.length() < 4) {
            index = -1;
            return;
        }
        int i = token.lastIndexOf('[');
        if (i < 0) {
            index = -1;
            return;
        }
        indexExpression = token.substring(i + 1, token.length() - 1);
        if (StringUtils.isNumeric(indexExpression))
            index = Integer.parseInt(indexExpression);
        this.token = token.substring(0, i);
    }
}
