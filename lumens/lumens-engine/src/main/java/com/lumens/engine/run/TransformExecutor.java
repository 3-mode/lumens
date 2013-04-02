/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.TransformComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformExecutor implements Executor
{
    private TransformComponent tComponent;
    private ExecuteContext executeContext;

    public TransformExecutor(TransformComponent tComponent,
                             ExecuteContext executeContext)
    {
        this.tComponent = tComponent;
        this.executeContext = executeContext;
    }

    @Override
    public List<Executor> execute()
    {
        List<Executor> tExList = new ArrayList<Executor>();
        List<ExecuteContext> exList = tComponent.execute(executeContext);
        if (tComponent.hasTarget())
        {
            Map<String, TransformComponent> targetList = tComponent.getTargetList();
            for (TransformComponent target : targetList.values())
            {
                for (ExecuteContext ctx : exList)
                {
                    if (target.accept(ctx))
                        tExList.add(new TransformExecutor(target, ctx));
                }
            }
        }
        if (!exList.isEmpty() && tExList.isEmpty())
        {
            // TODO need log system
            for (ExecuteContext ctx : exList)
            {
                System.out.println(String.format(
                        "No target component to process '%s'", ctx.getTargetName()));
            }
        }
        return tExList;
    }

    @Override
    public TransformComponent getTransformComponent()
    {
        return tComponent;
    }
}
