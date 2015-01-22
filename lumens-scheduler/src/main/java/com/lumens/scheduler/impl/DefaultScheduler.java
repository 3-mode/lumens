/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.scheduler.JobScheduler;
import com.lumens.scheduler.JobTrigger;
import com.lumens.engine.TransformProject;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.dao.RelationDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.entity.Job;
import com.lumens.sysdb.entity.Relation;
import com.lumens.sysdb.entity.Project;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.ScheduleBuilder;
import org.quartz.SchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultScheduler implements JobScheduler {
    boolean isStarted;
    Scheduler sched;
    List<DefaultJob> jobList = new ArrayList();
    Map<Long, DefaultJob> jobMap = new HashMap<>();
    Map<Long, JobTrigger> triggerMap = new HashMap<>();
    
    // TODO: maintain a running job list

    public DefaultScheduler() {
        isStarted = false;
        start();
    }

    public JobScheduler addSchedule(DefaultJob job, DefaultTrigger trigger) {
        if (jobMap.containsKey(job.getId())) {
            throw new RuntimeException("Job " + job.getId() + " already exist.");
        }

        jobList.add(job);
        jobMap.put(job.getId(), job);
        triggerMap.put(job.getId(), trigger);

        return this;
    }

    @Override
    public void resume() {
        try{
            loadFromDb();
        }catch (Exception ex){
            // TODO: log load job exception
        }
        schedule();
    }

    public void schedule() {
        for (DefaultJob job : jobList) {
            String group = job.getId().toString();
            List<TransformProject> projectList = job.getProjectList();
            for (TransformProject proj : projectList) {
                JobDetail jobDetail = newJob(DefaultJob.class)
                        .withIdentity(proj.getName(), group)
                        .build();

                JobTrigger defaultTrigger = triggerMap.get(job.getId());
                SimpleScheduleBuilder simpleBuilder = simpleSchedule();

                int repeatCount = defaultTrigger.getRepeatCount();
                if (repeatCount > 0) {
                    simpleBuilder.withRepeatCount(repeatCount);
                } else if (repeatCount < 0) {
                    simpleBuilder.repeatForever();
                }
                int repeatInterval = defaultTrigger.getRepeatInterval();
                if (repeatCount != 0 && repeatInterval > 0) {
                    simpleBuilder.withIntervalInSeconds(repeatInterval);
                }

                TriggerBuilder<Trigger> builder = newTrigger();
                builder.withIdentity(job.getId().toString(), group);
                builder.startNow();
                builder.withSchedule(simpleBuilder);
                builder.startAt(defaultTrigger.getStartTime());

                try {
                    sched.scheduleJob(jobDetail, builder.build());
                } catch (SchedulerException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public final void start() {
        try {
            if (!isStarted) {
                sched = new org.quartz.impl.StdSchedulerFactory().getScheduler();
                sched.start();
            }

        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void stop() {
        try {
            if (isStarted) {
                sched.shutdown();
            }
        } catch (SchedulerException ex) {
            // TODO: Print log
        }
    }

    public List<DefaultJob> getJobList() {
        return jobList;
    }

    public void loadFromDb() throws Exception {
        JobDAO jobDAO = DAOFactory.getJobDAO();
        RelationDAO projectRelationDAO = DAOFactory.getRelationDAO();
        ProjectDAO projectDAO = DAOFactory.getProjectDAO();
        List<Job> all = jobDAO.getAllJob();
        for (Job dbJob : all) {
            // Add job
            DefaultJob uiJob = new DefaultJob(dbJob.id, dbJob.name, dbJob.description);
            jobList.add(uiJob);
            
            // Add trigger
            JobTrigger uiTrigger = new DefaultTrigger(new Date(dbJob.startTime.getTime()), new Date(dbJob.endTime.getTime()), dbJob.repeatCount, dbJob.interval);
            triggerMap.put(dbJob.id, uiTrigger);

            // Add projects
            List<Relation> list = projectRelationDAO.getAllRelation(dbJob.id);
            for (Relation relation : list) {
                Project dbProject = projectDAO.getProject(relation.projectId);
                TransformProject uiProject = new TransformProject();
                new ProjectSerializer(uiProject).readFromJson(new ByteArrayInputStream(dbProject.data.getBytes()));
                uiJob.addProject(uiProject);
            }
        }
    }

    public void saveToDb() {

    }
}
