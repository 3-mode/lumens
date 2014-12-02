/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.TransformComponent;
import com.lumens.model.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformExecutor implements Executor {

    private final TransformComponent tComponent;
    private final ExecuteContext executeContext;

    public TransformExecutor(TransformComponent tComponent, ExecuteContext executeContext) {
        this.tComponent = tComponent;
        this.executeContext = executeContext;
    }

    @Override
    public List<Executor> execute() {
        List<Executor> tExList = new ArrayList<>();
        List<ExecuteContext> exList = tComponent.execute(executeContext);
        if (tComponent.hasTarget()) {
            Map<String, TransformComponent> targetList = tComponent.getTargetList();
            for (TransformComponent target : targetList.values()) {
                for (ExecuteContext ctx : exList) {
                    if (target.accept(ctx)) {
                        tExList.add(new TransformExecutor(target, ctx));
                    }
                }
            }
        }
        if (!exList.isEmpty() && tExList.isEmpty()) {
            // TODO need log system
            for (ExecuteContext ctx : exList) {
                for (ResultHandler handler : executeContext.getResultHandlers()) {
                    if (handler instanceof LastResultHandler)
                        handler.process(tComponent, ctx.getTargetFormatName(), (List<Element>) ctx.getInput());
                    // TODO need log system
                    System.out.println(String.format("No target component to process '%s'", ctx.getTargetFormatName()));
                }
            }
        }
        return tExList;
    }

    @Override
    public TransformComponent getTransformComponent() {
        return tComponent;
    }
}
