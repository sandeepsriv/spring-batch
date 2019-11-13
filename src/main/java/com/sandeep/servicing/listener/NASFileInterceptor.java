package com.sandeep.servicing.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import javax.batch.api.listener.JobListener;

public class NASFileInterceptor extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("After job execution");
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Before job execution");
    }

}
