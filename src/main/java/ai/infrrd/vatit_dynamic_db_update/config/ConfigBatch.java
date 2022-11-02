package ai.infrrd.vatit_dynamic_db_update.config;

import ai.infrrd.vatit_dynamic_db_update.entities.SupplierData;
import ai.infrrd.vatit_dynamic_db_update.helper.BlankLineRecordSeparatorPolicy;
import ai.infrrd.vatit_dynamic_db_update.repository.SupplierDB;
import ai.infrrd.vatit_dynamic_db_update.listener.InvoiceListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.function.Function;

@Configuration
@EnableBatchProcessing
public class ConfigBatch {
    //Reader class Object
    @Bean
    public FlatFileItemReader<SupplierData> reader() {

        FlatFileItemReader<SupplierData> reader= new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("/Untitled.csv"));
        // Reader. setResource(new FileSystemResource("D:/mydata/invoices.csv"));
        // reader.setResource(new UrlResource("https://xyz.com/files/invoices.csv"));
        // reader.setLinesToSkip(1);

        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter(DELIMITER_COMMA);
                setNames("studentId","firstName","secondName","emailId");
            }});

            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(SupplierData.class);
            }});
        }});

        reader.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());

        return reader;
    }

    //Autowire InvoiceRepository
    @Autowired
    SupplierDB repository;

    //Writer class Object
    @Bean
    public ItemWriter<SupplierData> writer(){
        // return new InvoiceItemWriter(); // Using lambda expression code instead of a separate implementation
        return supplierData -> {
            System.out.println("Saving Invoice Records: " +supplierData);
            repository.saveAll(supplierData);
        };
    }

    //Processor class Object
//    @Bean
//    public ItemProcessor<SupplierData, SupplierData> processor(){
//        // return new InvoiceProcessor(); // Using lambda expression code instead of a separate implementation
//        return invoice -> {
//            Double discount = invoice.getAmount()*(invoice.getDiscount()/100.0);
//            Double finalAmount= invoice.getAmount()-discount;
//            invoice.setFinalAmount(finalAmount);
//            return invoice;
//        };
//    }

    @Bean
    public UserItemProcessor processor(){
        return new UserItemProcessor();
    }

    //Listener class Object
    @Bean
    public JobExecutionListener listener() {
        return new InvoiceListener();
    }

    //Autowire StepBuilderFactory
    @Autowired
    private StepBuilderFactory sbf;

    //Step Object
    @Bean
    public Step stepA() {
        return sbf.get("stepA")
                .<SupplierData,SupplierData>chunk(1)
                .reader(reader())
                .processor((Function<? super SupplierData, ? extends SupplierData>) processor())
                .writer(writer())
                .build()
                ;
    }

    //Autowire JobBuilderFactory
    @Autowired
    private JobBuilderFactory jbf;

    //Job Object
    @Bean
    public Job jobA(){
        return jbf.get("jobA")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .start(stepA())
                .build();
    }
}
