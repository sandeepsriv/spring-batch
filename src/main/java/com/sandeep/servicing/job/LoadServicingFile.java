package com.sandeep.servicing.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandeep.servicing.dto.LoanServicingDTO;
import com.sandeep.servicing.mapper.ServicingCSVMapper;
import com.sandeep.servicing.model.LoanServicing;
import com.sandeep.servicing.processor.LoanServicingJSONProcessor;
import com.sandeep.servicing.processor.LoanServicingCSVProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;

@Configuration
public class LoadServicingFile {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private LoanServicingCSVProcessor loanServicingCSVProcessor;
    private LoanServicingJSONProcessor loanServicingJSONProcessor;
    private DataSource dataSource;

    @Autowired
    public LoadServicingFile(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, LoanServicingJSONProcessor loanServicingJSONProcessor, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.loanServicingJSONProcessor = loanServicingJSONProcessor;
        this.dataSource = dataSource;
    }

    @Qualifier(value = "ServicingLoad")
    @Bean
    public Job servicingJob() throws Exception {
        return this.jobBuilderFactory.get("ServicingLoad")
                .start(step1ServicingJob())
                .build();
    }

    @Bean
    public Step step1ServicingJob() throws Exception {
        return this.stepBuilderFactory.get("step1")
                .<LoanServicingDTO, LoanServicing>chunk(5)
                .reader(loanJSONReader())
                .processor(loanServicingJSONProcessor)
                .writer(loanServicingDBWriterDefault())
                //.taskExecutor(taskExecutor()) -> uncomment this when you want to consume file using multiple threads
                .build();
    }

    @Bean
    @StepScope
    Resource inputFileResource(@Value("#{jobParameters[fileName]}") final String fileName) throws Exception {
        return new ClassPathResource(fileName);
    }

    @Bean
    @StepScope
    public FlatFileItemReader<LoanServicingDTO> loanReader() throws Exception {
        FlatFileItemReader<LoanServicingDTO> reader = new FlatFileItemReader<>();
        reader.setResource(inputFileResource(null));
        reader.setLineMapper(new DefaultLineMapper<LoanServicingDTO>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("loanIdSysGen", "upb", "noteRate", "loanStatus", "mortgageType");
                setDelimiter(",");
            }});
            setFieldSetMapper(new ServicingCSVMapper());
        }});
        return reader;
    }

    @Bean
    @StepScope
    public JsonItemReader<LoanServicingDTO> loanJSONReader() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JacksonJsonObjectReader jacksonJsonObjectReader = new JacksonJsonObjectReader(LoanServicingDTO.class);
        jacksonJsonObjectReader.setMapper(mapper);

        JsonItemReader<LoanServicingDTO> reader = new JsonItemReader(inputFileResource(null), jacksonJsonObjectReader);
        reader.setName("inputReader");

        return reader;
    }


    @Bean
    public JdbcBatchItemWriter<LoanServicing> loanServicingDBWriterDefault() {

        JdbcBatchItemWriter<LoanServicing> itemWriter = new JdbcBatchItemWriter<LoanServicing>();
        itemWriter.setDataSource(dataSource);
        System.out.println("#JdbcBatchItemWriter START");
        itemWriter.setSql("insert into loan_servicing (loan_id_sys_gen, upb, note_rate, loan_status, mortgage_type) values (:loanIdSysGen, :upb, :noteRate, :loanStatus, :mortgageType)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<LoanServicing>());
        System.out.println("#JdbcBatchItemWriter END");
        return itemWriter;

    }

    @Bean
    public TaskExecutor taskExecutor () {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(5);
        return simpleAsyncTaskExecutor;
    }


}
