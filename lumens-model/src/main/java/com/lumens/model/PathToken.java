/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

/**
 *
 * @author shaofeng wang
 */
public class PathToken
{
    private String token;
    private int index;

    public PathToken(String token)
    {
        this.token = token;
        parseToken(token);
    }

    public boolean isIndexed()
    {
        return index != -1;
    }

    public int index()
    {
        return index;
    }

    @Override
    public String toString()
    {
        return token;
    }

    private void parseToken(String token)
    {
        if (token.charAt(token.length() - 1) != ']' || token.length() < 4)
        {
            index = -1;
            return;
        }
        int i = token.lastIndexOf('[');
        if (i < 0)
        {
            index = -1;
            return;
        }
        index = Integer.parseInt(token.substring(i + 1, token.length() - 1));
        this.token = token.substring(0, i);
    }
}
