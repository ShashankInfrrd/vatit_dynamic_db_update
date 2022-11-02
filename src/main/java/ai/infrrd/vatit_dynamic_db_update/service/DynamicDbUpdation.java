package ai.infrrd.vatit_dynamic_db_update.service;

import ai.infrrd.vatit_dynamic_db_update.entities.FileUploadResponse;
import ai.infrrd.vatit_dynamic_db_update.config.JobRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DynamicDbUpdation extends Thread {

    @Autowired
    private ThreadParkingService threadParkingService;

//    @Autowired
//    private SupplierDB supplierDB;

//    @Autowired
//    private SupplierDbService supplierDbService;

    @Autowired
    private JobRunner jobRunner;

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDbUpdation.class);
    String dynamicUpdation(FileUploadResponse fileUploadResponse, Resource resource, String fileName)
    {
        new Thread(()->{
            try {
                System.out.println("Entered thread");
                String requestId = Objects.requireNonNull(fileUploadResponse).getRequestId();
                display(requestId);

                }catch (Exception e){
                   System.out.println("dfy "+ e);
                }
                    System.out.println("Completed thread sleep");
                }).start();
        return "String4554";
    }

    public void display(String requestId)
    {
        try {
            Thread.sleep(6000);
//            System.out.println(show());
//            System.out.println(clone());
            jobRunner.run();
            System.out.println("Displayed for "+requestId);
        }
        catch (Exception e)
        {
            System.out.println("err "+ e);
        }
    }

//    public List<SupplierData> show()
//    {
//
//        return supplierDB.findAll();
//    }

//    public List<SupplierData> clone()
//    {
//        return supplierDbService.cloneTable();
//    }

//    public void willBeCalledAsCallback(FileUploadResponse fileUploadResponse)
//    {
//        String requestId = Objects.requireNonNull(fileUploadResponse).getRequestId();
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        ResponseObject responseObject=new ResponseObject("1212ddedw444","dddd",timestamp,"processed");
//        if(threadParkingService.isThreadPresent(fileUploadResponse.getRequestId())) {
//            LOGGER.info("Received callback data for requestId {} and thread is active ", requestId);
//            threadParkingService.setCallBackData(responseObject);
//            if (threadParkingService.shouldInvoke(responseObject.getRequestId())) {
//                threadParkingService.invokeThread(requestId);
//            }
//        } else {
//            LOGGER.info("Received callback data for requestId {} but thread has been removed ", requestId);
//        }
//    }
}
