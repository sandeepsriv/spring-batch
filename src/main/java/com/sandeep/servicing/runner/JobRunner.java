package com.sandeep.servicing.runner;

import com.sandeep.servicing.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobRunner {

    private static final Logger logger = LoggerFactory.getLogger(JobRunner.class);
    private JobLauncher simpleJobLauncher;
    private Job servicingJob;
    private ExecutionContext executionContext;

    @Autowired
    public JobRunner(Job servicingJob, JobLauncher jobLauncher, ExecutionContext executionContext) {
        this.simpleJobLauncher = jobLauncher;
        this.servicingJob = servicingJob;
        this.executionContext = executionContext;
    }

    @Async
    public void runBatchJob() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(Constants.FILE_NAME_CONTEXT_KEY, "loanservicing.json");
        jobParametersBuilder.addDate("date", new Date(), true);
        executionContext.putString("#NAS_Filename", "loanservicing.json");
        runJob(servicingJob, jobParametersBuilder.toJobParameters());
    }


    public void runJob(Job job, JobParameters parameters) {
        try {

            JobExecution jobExecution = simpleJobLauncher.run(job, parameters);

        } catch (JobExecutionAlreadyRunningException e) {

            logger.info("Job with fileName={} is already running.", parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));

        } catch (JobRestartException e) {

            logger.info("Job with fileName={} was not restarted.", parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));

        } catch (JobInstanceAlreadyCompleteException e) {

            logger.info("Job with fileName={} already completed.", parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));

        } catch (JobParametersInvalidException e) {

            logger.info("Invalid job parameters.", parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));

        }
    }


}