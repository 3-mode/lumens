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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.wang@outlook.com)
 */
public class SequenceTransformExecuteJob implements ExecuteJob, Runnable {

    private TransformProject project;
    private Thread currentThread;
    private List<ResultHandler> handlers;

    class SingleThreadExecuteStack extends LinkedList<ExecuteContext> {

        public void push(List<ExecuteContext> executorList) {
            super.addAll(0, executorList);
        }
    }

    public SequenceTransformExecuteJob(TransformProject project, List<ResultHandler> handlers) {
        this.project = project;
        this.handlers = handlers;
    }

    public SequenceTransformExecuteJob(TransformProject project) {
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
            project.open();
            this.executeTransform(project);
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

    protected void executeTransform(TransformProject transformProject) {
        try {
            List<StartEntry> startList = transformProject.getStartEntryList();
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}