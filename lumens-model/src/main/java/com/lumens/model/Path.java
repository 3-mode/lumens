/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.Iterator;

/**
 *
 * @author washaofe
 */
public interface Path {

    public boolean isEmpty();

    public PathToken token(int index);

    public int tokenCount();

    public Iterator<PathToken> iterator();

    public Path addLeft(String token);

    public Path addRight(String token);

    public Path left(int count);

    public Path right(int count);

    public Path removeLeft(int count);

    public Path removeRight(int count);
}
