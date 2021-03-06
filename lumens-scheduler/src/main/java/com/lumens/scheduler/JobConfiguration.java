/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.engine.TransformProject;
import com.lumens.engine.handler.InspectionHandler;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface JobConfiguration {
    public enum Repeat {
        Never(0),
        Secondly(1),
        Minutely(2),
        Hourly(3),
        Daily(4),
        Weekly(5),
        Monthly(6),
        Yearly(7);

        private int value = 0;

        private Repeat(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Repeat valueOf(int value) {
            switch (value) {
                case 1:
                    return Secondly;
                case 2:
                    return Minutely;
                case 3:
                    return Hourly;
                case 4:
                    return Daily;
                case 5:
                    return Weekly;
                case 6:
                    return Monthly;
                case 7:
                    return Yearly;
                default:
                    return Never;
            }
        }
    };

    public Long getId();

    public String getName();

    public String getDescription();

    public int getRepeat();

    public long getStartTime();

    public long getEndTime();

    public int getInterval();

    public JobConfiguration addProject(TransformProject project);

    public JobConfiguration addProject(List<TransformProject> projects);

    public List<TransformProject> getProjectList();

    public void setInspectionHandlers(List<InspectionHandler> handlers);

    public List<InspectionHandler> getInspectionHandlers();

    public List<InspectionHandler> getInspectionHandlers(TransformProject project);

    public void verfiyAssociatedProjects();

    public void stop();

    public Logger getLogger();

    public boolean hasLogger();
}
