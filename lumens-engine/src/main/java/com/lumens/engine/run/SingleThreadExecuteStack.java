/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.ExecuteContext;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class SingleThreadExecuteStack extends LinkedList<ExecuteContext> {

    public void push(List<ExecuteContext> executorList) {
        super.addAll(0, executorList);
    }
}
