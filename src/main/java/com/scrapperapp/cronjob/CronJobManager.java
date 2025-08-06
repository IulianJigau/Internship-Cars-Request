package com.scrapperapp.cronjob;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class CronJobManager {
    private final Scheduler scheduler;

    public CronJobManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addJob(Class<? extends Job> jobClass, String taskName, String expression) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(jobClass)
            .withIdentity(taskName, "group1")
            .build();

        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(taskName + "_trigger", "group1")
            .withSchedule(CronScheduleBuilder.cronSchedule(expression))
            .forJob(job)
            .build();

        scheduler.scheduleJob(job, trigger);
    }
}
