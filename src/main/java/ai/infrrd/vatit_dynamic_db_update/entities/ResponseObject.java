package ai.infrrd.vatit_dynamic_db_update.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResponseObject {
    String RequestId;

    @JsonIgnore
    String jobId;
    String fileName;
    Timestamp timestamp;

    String status;

    public ResponseObject(String id, String jobId, String fileName, Timestamp timestamp, String status) {
        this.RequestId = id;
        this.jobId=jobId;
        this.fileName = fileName;
        this.timestamp = timestamp;
        this.status = status;
    }
}
