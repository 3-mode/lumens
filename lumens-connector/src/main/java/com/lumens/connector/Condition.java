/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

/**
 *
 * @author shaofeng wang
 */
public interface Condition
{
    public Condition or(Condition c);

    public Condition and(Condition c);

    public Condition not(Condition c);

    public String getCondition();
}
