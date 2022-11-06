package ai.infrrd.vatit_dynamic_db_update.exception;

public class InvalidFileArgsException extends RuntimeException {
    private String message;

    public InvalidFileArgsException(String message) {
        super(message);
        this.message = message;
    }
}
