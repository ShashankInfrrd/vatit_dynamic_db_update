package ai.infrrd.vatit_dynamic_db_update.controller;

import ai.infrrd.vatit_dynamic_db_update.entities.FailedResponse;
import ai.infrrd.vatit_dynamic_db_update.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.io.IOException;

@ControllerAdvice
public class VatitDbUpdateControllerAdvice {

    private static final String BAD_REQUEST = "BAD_REQUEST";

    public static final String UPLOAD_FAILED = "UPLOAD FAILED";
    private static final Logger LOGGER = LoggerFactory.getLogger(VatitDbUpdateControllerAdvice.class);

//    @ExceptionHandler(FileFormatException.class)
//    public ResponseEntity<?> handleFileFormatException(FileFormatException fileFormatException) {
//        LOGGER.warn(fileFormatException.getMessage());
//        return new ResponseEntity<>(new SedgwickFailedResponse(Constant.UPLOAD_FAILED, fileFormatException.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
    @ExceptionHandler(FileSaveException.class)
    public ResponseEntity<?> handleUnableToSaveFileException(FileSaveException fileSaveException) {
        LOGGER.warn(fileSaveException.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidFileArgsException.class)
    public ResponseEntity<?> handleInvalidFileArgsException(InvalidFileArgsException invalidFileArgsException) {
        LOGGER.warn(invalidFileArgsException.getMessage());
        return new ResponseEntity<>(new FailedResponse(UPLOAD_FAILED, "Invalid multipart file "), HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(MultipartException.class)
//    public ResponseEntity<?> handleMultiPartException(MultipartException multipartException) {
//        return new ResponseEntity<>(new SedgwickFailedResponse(Constant.UPLOAD_FAILED, "Invalid multipart file "), HttpStatus.BAD_REQUEST);
//    }
//
    @ExceptionHandler(InvalidApiKeyFormatException.class)
    public ResponseEntity<?> handleInvalidApiKey(InvalidApiKeyFormatException invalidApiKeyFormatException) {
        LOGGER.warn(invalidApiKeyFormatException.getMessage());
        return new ResponseEntity<>(new FailedResponse(UPLOAD_FAILED, invalidApiKeyFormatException.getMessage()), HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(UpstreamServerException.class)
//    public ResponseEntity<?> handleUpstreamServerException(UpstreamServerException upstreamServerException) {
//        if (upstreamServerException.getTitanUploadResponse() == null) {
//            return new ResponseEntity<>(upstreamServerException.getSedgwickFailedResponse(), upstreamServerException.getStatus());
//        }
//        return new ResponseEntity<>(upstreamServerException.getTitanUploadResponse(), upstreamServerException.getStatus());
//    }
//
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<SedgwickFailedResponse> badRequestExceptionHandler(BadRequestException badRequestException) {
//        return new ResponseEntity<>(new SedgwickFailedResponse(BAD_REQUEST, badRequestException.getMessage()), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIoException(IOException ioException) {
        LOGGER.warn("IOException caught {}", ioException.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileCountException.class)
    public ResponseEntity<?> invalidFileCount(FileCountException fileCountException) {
        LOGGER.warn(fileCountException.getMessage());
        return new ResponseEntity<>(new FailedResponse(BAD_REQUEST, fileCountException.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
