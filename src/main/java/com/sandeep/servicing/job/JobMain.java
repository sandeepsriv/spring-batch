package com.sandeep.servicing.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandeep.servicing.dto.LoanDTO;
import com.sandeep.servicing.listener.NASFileInterceptor;
import com.sandeep.servicing.listener.PerRecordInterceptor;
import com.sandeep.servicing.mapper.LoanCSVMapper;
import com.sandeep.servicing.model.Loan;
import com.sandeep.servicing.processor.LoanCSVProcessor;
import com.sandeep.servicing.processor.LoanJSONProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
public class JobMain {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private LoanCSVProcessor loanCSVProcessor;
    private LoanJSONProcessor loanJSONProcessor;
    private DataSource dataSource;

    @Autowired
    public JobMain(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, LoanJSONProcessor loanJSONProcessor, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.loanJSONProcessor = loanJSONProcessor;
        this.dataSource = dataSource;
    }

    @Qualifier(value = "ServicingLoad")
    @Bean
    public Job servicingJob() throws Exception {
        return this.jobBuilderFactory.get("ServicingLoad")
                .start(step1ServicingJob())
                .listener(servicingListener())
                .build();
    }

    @Bean
    public Step step1ServicingJob() throws Exception {
        return this.stepBuilderFactory.get("step1")
                .<LoanDTO, Loan>chunk(1)
                .reader(loanJSONReader())
                .processor(loanJSONProcessor)
                .writer(loanServicingDBWriterDefault())
                //.taskExecutor(taskExecutor()) -> uncomment this when you want to consume file using multiple threads
                .faultTolerant().skipPolicy(skipWhenError())
                .listener(individualRecordListener())
                .build();
    }

    @Bean
    @StepScope
    Resource inputFileResource(@Value("#{jobParameters[fileName]}") final String fileName) throws Exception {
        return new ClassPathResource(fileName);
    }

    @Bean
    @StepScope
    public FlatFileItemReader<LoanDTO> loanReader() throws Exception {
        FlatFileItemReader<LoanDTO> reader = new FlatFileItemReader<>();
        reader.setResource(inputFileResource(null));
        reader.setLineMapper(new DefaultLineMapper<LoanDTO>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("loanIdSysGen", "upb", "noteRate", "loanStatus", "mortgageType");
                setDelimiter(",");
            }});
            setFieldSetMapper(new LoanCSVMapper());
        }});
        return reader;
    }

    @Bean
    @StepScope
    public JsonItemReader<LoanDTO> loanJSONReader() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JacksonJsonObjectReader jacksonJsonObjectReader = new JacksonJsonObjectReader(LoanDTO.class);
        jacksonJsonObjectReader.setMapper(mapper);

        JsonItemReader<LoanDTO> reader = new JsonItemReader(inputFileResource(null), jacksonJsonObjectReader);
        reader.setName("inputReader");

        return reader;
    }


    @Bean
    public JdbcBatchItemWriter<Loan> loanServicingDBWriterDefault() {

        JdbcBatchItemWriter<Loan> itemWriter = new JdbcBatchItemWriter<Loan>();
        itemWriter.setDataSource(dataSource);
        System.out.println("#JdbcBatchItemWriter START");
        itemWriter.setSql("insert into loan_servicing (loan_id_sys_gen, upb, note_rate, loan_status, mortgage_type) values (:loanIdSysGen, :upb, :noteRate, :loanStatus, :mortgageType)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Loan>());
        System.out.println("#JdbcBatchItemWriter END");
        return itemWriter;

    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(5);
        return simpleAsyncTaskExecutor;
    }

    @Bean
    public JobRecordSkipPolicy skipWhenError() {

        return new JobRecordSkipPolicy();

    }

    @Bean
    public NASFileInterceptor servicingListener() {
        return new NASFileInterceptor();
    }

    @Bean
    public PerRecordInterceptor individualRecordListener() {
        return new PerRecordInterceptor();
    }
}
