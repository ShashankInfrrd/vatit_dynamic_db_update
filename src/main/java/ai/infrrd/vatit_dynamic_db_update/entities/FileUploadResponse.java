package ai.infrrd.vatit_dynamic_db_update.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadResponse {
    String RequestId;
    String fileName;
    Timestamp timestamp;

    public FileUploadResponse(String id, String fileName, Timestamp timestamp) {
        this.RequestId = id;
        this.fileName = fileName;
        this.timestamp = timestamp;
    }
}
