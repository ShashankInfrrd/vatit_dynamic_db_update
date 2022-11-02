package ai.infrrd.vatit_dynamic_db_update.entities;

import lombok.Data;

@Data
public class RequestThreadInfo {
    private ResponseObject response;
    private Thread thread;

    public RequestThreadInfo(Thread thread) {
        this.thread = thread;
    }
}
