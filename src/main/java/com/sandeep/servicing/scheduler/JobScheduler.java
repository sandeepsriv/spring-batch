package com.sandeep.servicing.scheduler;

import com.sandeep.servicing.runner.JobRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class JobScheduler {

    private JobRunner jobRunner;

    public JobScheduler(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }

    @Scheduled(cron = "0 0/2 * 1/1 * ?")
    public void jobScheduled() {
        System.out.println("Job triggered!");
        jobRunner.runBatchJob();
    }
}
