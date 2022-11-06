package ai.infrrd.vatit_dynamic_db_update.service;

import ai.infrrd.vatit_dynamic_db_update.entities.FileUploadResponse;

import ai.infrrd.vatit_dynamic_db_update.entities.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DynamicDbUpdation extends Thread {

    private final ConcurrentHashMap<String, ResponseObject> statusOfJob = new ConcurrentHashMap<>();
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job job;

    @Autowired
    private SupplierDbService supplierDbService;

    @Lazy
    @Autowired
    private FileService fileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDbUpdation.class);
    void dynamicUpdation(FileUploadResponse fileUploadResponse, Resource resource, String fileName)
    {
        new Thread(()->{
            try {
                System.out.println("Entered thread");
                System.out.println(resource.getURL());
                // ToDo: Clone the table first

                String requestId = Objects.requireNonNull(fileUploadResponse).getRequestId();
                // Emptying the table.
                supplierDbService.truncateMyTable();

                load(fileUploadResponse.getRequestId(),resource);

                }catch (Exception e){
                   System.out.println("dfy "+ e);
                }finally {
                System.out.println();
                fileService.deleteFiles(fileName);
            }
                    System.out.println("Completed thread sleep");
                }).start();
    }

    public BatchStatus load(String requestId,Resource resource) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        maps.put("file_source", new JobParameter(String.valueOf(resource.getURL())));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ResponseObject responseObject=new ResponseObject(requestId,jobExecution.getJobId().toString(), resource.getFilename(),timestamp,"processing");
        statusOfJob.put(requestId,responseObject);
        System.out.println("Batch is Running...");
        while (jobExecution.isRunning()) {
            System.out.println("...");
        }
        statusOfJob.get(requestId).setStatus(jobExecution.getStatus().toString());

        return jobExecution.getStatus();
    }

    public ResponseObject jobStatus(String requestId)
    {
        return statusOfJob.get(requestId);
    }
}
