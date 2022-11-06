package ai.infrrd.vatit_dynamic_db_update.controller;

import ai.infrrd.vatit_dynamic_db_update.entities.FileUploadResponse;
import ai.infrrd.vatit_dynamic_db_update.exception.InvalidApiKeyFormatException;
import ai.infrrd.vatit_dynamic_db_update.exception.InvalidFileArgsException;
import ai.infrrd.vatit_dynamic_db_update.service.DynamicDbUpdation;
import ai.infrrd.vatit_dynamic_db_update.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/")
@Slf4j
@RestController
public class VatitDbUpdateController {

    @Autowired
    private FileService fileService;
    @Autowired
    private DynamicDbUpdation dynamicDbUpdation;

    private static final Logger LOGGER = LoggerFactory.getLogger(VatitDbUpdateController.class);
    private static final String KEY = "apikey";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestHeader Map<String, String> httpHeader) throws IOException {
        if (preProcessFileName(multipartFile) && processHeader(httpHeader)) {
            String apiKey = httpHeader.get(KEY);
            FileUploadResponse responseEntity = fileService.handleFile(multipartFile, apiKey);
            LOGGER.info("Upload requestId {} ", responseEntity.getRequestId());
            return new ResponseEntity<>(responseEntity, HttpStatus.OK);
        } else {
            throw new InvalidFileArgsException("File list empty ");
        }
    }

    @GetMapping("/showStatus/{scanId}")
    public ResponseEntity<?> showStatus(@PathVariable("scanId") String scanId)
    {
        return new ResponseEntity<>(dynamicDbUpdation.jobStatus(scanId), HttpStatus.OK);
    }

    private boolean preProcessFileName(MultipartFile multipartFile) {
            if (Objects.requireNonNull(multipartFile.getOriginalFilename()).isBlank() || multipartFile.isEmpty()) {
                return false;
            }
        return true;
    }

    private boolean processHeader(Map<String, String> httpHeader) {
        if (httpHeader.containsKey(KEY) && !httpHeader.get(KEY).isBlank())
            return true;
        else {
            throw new InvalidApiKeyFormatException("Invalid api/value pair ");
        }
    }
}
