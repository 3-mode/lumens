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
public class TransformSingleThreadEngine {
    public void execute(TransformProject project) {
        List<StartEntry> startList = project.getStartEntryList();
        for (StartEntry entry : startList) {
            SingleThreadExecuteStack executorStack = new SingleThreadExecuteStack();
            executorStack.push(new TransformExecutor(entry.getStartComponent(), new TransformExecuteContext(null, entry.getStartName())));
            while (!executorStack.isEmpty()) {
                Executor executor = executorStack.pop();
                List<Executor> tExList = executor.execute();
                executorStack.push(tExList);
            }
        }
    }
}
