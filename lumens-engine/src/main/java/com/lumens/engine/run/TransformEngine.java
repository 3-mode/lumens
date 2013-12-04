/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.TransformProject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformEngine {
    private List<ExecuteJob> jobList = new ArrayList<>();

    public void execute(TransformProject project) throws Exception {
        ExecuteJob job = new SingleThreadTransformExecuteJob(project);
        jobList.add(job);
        job.run();
    }

    public int getJobCount() {
        return jobList.size();
    }
}
