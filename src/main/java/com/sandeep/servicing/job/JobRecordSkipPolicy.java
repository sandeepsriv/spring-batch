package com.sandeep.servicing.job;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class JobRecordSkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {

        // Variable skipCount's value starts with 0. If you have one bad record it will be set to 0.
        // If majority of records (e.g. > 5) are failing then may be you want to
        // Fail the entire job else you want the jo to succeed
        return (skipCount > 5) ? false : true;

    }
}
