package ai.infrrd.vatit_dynamic_db_update.service;

import ai.infrrd.vatit_dynamic_db_update.entities.FileUploadResponse;
import ai.infrrd.vatit_dynamic_db_update.entities.RequestThreadInfo;
import ai.infrrd.vatit_dynamic_db_update.entities.ResponseObject;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ThreadParkingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadParkingService.class);

    private final ConcurrentHashMap<String, RequestThreadInfo> THREAD_INFO = new ConcurrentHashMap<>();

    @Value("${thread.wait.time}")
    private long MAX_TIME;


    public void parkThread(FileUploadResponse responseEntity) {
        String requestId = Objects.requireNonNull(responseEntity).getRequestId();
        THREAD_INFO.put(requestId, new RequestThreadInfo(Thread.currentThread()));
        try {
            Thread.sleep(MAX_TIME);
            LOGGER.warn("Failed to receive callback data for requestId {} in time ", requestId);
            removeThreadInfo(requestId);
            LOGGER.info("Titan upload response {} ", new Gson().toJson(responseEntity));
//            throw new UpstreamServerException(new SedgwickFailedResponse(Constant.FAILED, Constant.TIME_OUT_MSG + requestId), HttpStatus.GATEWAY_TIMEOUT);
        } catch (InterruptedException e) {
            LOGGER.info("Received thread interrupt for requestId {}", requestId);
        }
    }

    public void removeThreadInfo(String requestId) {
        THREAD_INFO.remove(requestId);
        LOGGER.info("Removed thread info for requestId {} ", requestId);
    }

    public ResponseObject getCallBackData(String requestId) {
        return Objects.requireNonNull(THREAD_INFO.computeIfPresent(requestId, (key, val) -> val)).getResponse();
    }

    public void setCallBackData(ResponseObject titanObject) {
        if(null == THREAD_INFO.get(titanObject.getRequestId()).getResponse()) {
            THREAD_INFO.get(titanObject.getRequestId()).setResponse(titanObject);
        }else{
            ResponseObject storedResponse = THREAD_INFO.get(titanObject.getRequestId()).getResponse();
//            storedResponse.getDocuments().add(titanObject.getDocuments().get(0));
        }
//        int callBackCount = THREAD_INFO.get(titanObject.getRequestId()).getCallBackCount();
//        THREAD_INFO.get(titanObject.getRequestId()).setCallBackCount(callBackCount - 1);
    }

    public boolean isThreadPresent(String requestId) {
        return THREAD_INFO.containsKey(requestId);
    }

    public void invokeThread(String requestId) {
        THREAD_INFO.get(requestId).getThread().interrupt();
    }

    public boolean shouldInvoke(String requestId) {
//        return THREAD_INFO.get(requestId).getCallBackCount() == 0;
        return true;
    }
}
