package ai.infrrd.vatit_dynamic_db_update.entities;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResponseObject {
    String RequestId;
    String fileName;
    Timestamp timestamp;

    String status;

    public ResponseObject(String id, String fileName, Timestamp timestamp, String status) {
        this.RequestId = id;
        this.fileName = fileName;
        this.timestamp = timestamp;
        this.status = status;
    }
}
