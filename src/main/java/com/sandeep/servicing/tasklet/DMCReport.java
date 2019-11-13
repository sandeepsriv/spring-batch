package com.sandeep.servicing.tasklet;

import com.sandeep.servicing.utils.Constants;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DMCReport implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try (Stream<String> loans = Files.lines(Paths.get("/Users/sandeepsriv/GitHub/spring-batch/src/main/resources/loanservicing.json"))) {

             System.out.println("Send DMC Report");

        }
        return null;
    }
}
