package ai.infrrd.vatit_dynamic_db_update.entities;

public class FailedResponse {
    private String STATUS;
    private String message;

    public FailedResponse(String STATUS, String message) {
        this.STATUS = STATUS;
        this.message = message;
    }
}
