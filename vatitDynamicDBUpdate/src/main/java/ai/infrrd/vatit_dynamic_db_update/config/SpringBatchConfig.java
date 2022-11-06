package ai.infrrd.vatit_dynamic_db_update.config;

import ai.infrrd.vatit_dynamic_db_update.batch.JobListener;
import ai.infrrd.vatit_dynamic_db_update.model.DataSupplier;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.annotation.Transactional;


@Configuration
@EnableBatchProcessing
@Transactional
public class SpringBatchConfig {

    private String filePath;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<DataSupplier> itemReader,
                   ItemProcessor<DataSupplier, DataSupplier> itemProcessor,
                   ItemWriter<DataSupplier> itemWriter,
                   JobListener jobExecutionListener){
        Step step=stepBuilderFactory.get("ETL-file-load")
                .<DataSupplier, DataSupplier>chunk(1)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
        return jobBuilderFactory.get("ETL-load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<DataSupplier> itemReader(@Value("#{jobParameters['file_source']}") String resource)
    {
        System.out.println("fielPath is "+ resource);
        FlatFileItemReader<DataSupplier> flatFileItemReader=new FlatFileItemReader<>();
//        flatFileItemReader.setResource(new FileSystemResource("/home/shashanksunil/vatit_dynamic_db_update /src/main/resources/Untitled.csv"));
        flatFileItemReader.setResource(new FileSystemResource(resource.substring(5)));

        flatFileItemReader.setName("CSV-Response");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<DataSupplier> lineMapper() {
        DefaultLineMapper<DataSupplier> lineMapper= new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer= new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"studentId","firstName","secondName","emailId"});
        lineTokenizer.setIncludedFields(new int[]{0,1,2,3});
        BeanWrapperFieldSetMapper<DataSupplier> fieldSetMapper =new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(DataSupplier.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}
