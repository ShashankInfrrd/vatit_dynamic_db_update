package ai.infrrd.vatit_dynamic_db_update.exception;

import java.io.IOException;

public class FileSaveException extends IOException {
    String message;

    public FileSaveException(String message) {
        super(message);
        this.message = message;
    }
}
