/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class SingleThreadExecuteStack extends LinkedList<Executor>
{
    public void push(List<Executor> executorList)
    {
        super.addAll(0, executorList);
    }
}
