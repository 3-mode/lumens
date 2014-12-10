/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.ExecuteContext;
import com.lumens.engine.StartEntry;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.TransformProject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.wang@outlook.com)
 */
public class SingleThreadTransformExecuteJob implements ExecuteJob, Runnable {

    private TransformProject project;
    private Thread currentThread;
    private List<ResultHandler> handlers;

    public SingleThreadTransformExecuteJob(TransformProject project, List<ResultHandler> handlers) {
        this.project = project;
        this.handlers = handlers;
    }

    public SingleThreadTransformExecuteJob(TransformProject project) {
        this(project, new ArrayList<ResultHandler>(1));
    }

    @Override
    public void execute() {
        currentThread = new Thread(this);
        currentThread.start();
    }

    @Override
    public void run() {
        try {
            if (!project.isOpen())
                project.open();
            List<StartEntry> startList = project.getStartEntryList();
            for (StartEntry entry : startList) {
                SingleThreadExecuteStack executorStack = new SingleThreadExecuteStack();
                executorStack.push(new TransformExecuteContext(entry.getStartComponent(), entry.getStartFormatName(), handlers));
                while (!executorStack.isEmpty()) {
                    ExecuteContext exectueCtx = executorStack.pop();
                    TransformComponent exeComponent = exectueCtx.getTargetComponent();
                    List<ExecuteContext> exList = exeComponent.execute(exectueCtx);
                    if (!exList.isEmpty())
                        executorStack.push(exList);
                }
            }
            project.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void join() {
        if (currentThread != null)
            try {
                currentThread.join();
            } catch (InterruptedException ex) {
            }
    }
}
