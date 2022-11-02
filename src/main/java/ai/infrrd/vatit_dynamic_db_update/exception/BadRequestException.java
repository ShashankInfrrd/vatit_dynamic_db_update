package ai.infrrd.vatit_dynamic_db_update.exception;

public class BadRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }


    public BadRequestException(String message, Throwable thrw) {
        super(message, thrw);
    }

}
