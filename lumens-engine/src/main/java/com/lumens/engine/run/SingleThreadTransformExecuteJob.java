/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.StartEntry;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.TransformProject;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class SingleThreadTransformExecuteJob implements ExecuteJob {
    private TransformProject project;

    public SingleThreadTransformExecuteJob(TransformProject project) {
        this.project = project;
    }

    @Override
    public void run() throws Exception {
        if (!project.isOpen())
            project.open();
        List<StartEntry> startList = project.getStartEntryList();
        for (StartEntry entry : startList) {
            SingleThreadExecuteStack executorStack = new SingleThreadExecuteStack();
            executorStack.push(new TransformExecutor(entry.getStartComponent(), new TransformExecuteContext(entry.getStartName())));
            while (!executorStack.isEmpty()) {
                Executor executor = executorStack.pop();
                List<Executor> tExList = executor.execute();
                executorStack.push(tExList);
            }
        }
        project.close();
    }
}