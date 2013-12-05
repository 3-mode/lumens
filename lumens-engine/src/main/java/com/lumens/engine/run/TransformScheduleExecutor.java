/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.TransformComponent;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformScheduleExecutor extends TransformExecutor {

    public TransformScheduleExecutor(TransformComponent tComponent, ExecuteContext executeContext) {
        super(tComponent, executeContext);
    }

    @Override
    public List<Executor> execute() {
        // TODO compute the scheduler time
        return super.execute();
    }
}
