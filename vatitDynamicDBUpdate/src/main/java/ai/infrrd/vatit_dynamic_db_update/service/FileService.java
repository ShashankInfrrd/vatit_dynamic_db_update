package ai.infrrd.vatit_dynamic_db_update.service;

import ai.infrrd.vatit_dynamic_db_update.entities.FileUploadResponse;
import ai.infrrd.vatit_dynamic_db_update.exception.FileSaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.*;

@Service
public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    @Value("${upload.path}")
    private String uploadDirectory;

    @Autowired
    private DynamicDbUpdation dynamicDbUpdation;

    public FileUploadResponse handleFile(MultipartFile multipartFile, String apiKey) throws IOException {
        FileUploadResponse fileUploadResponse;
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        saveFile(fileName,multipartFile,uploadDirectory);
        fileUploadResponse = initialFileUpload(fileName);
        Resource fileResource = getFileResource(fileName);
        dynamicDbUpdation.dynamicUpdation(fileUploadResponse,fileResource,fileName);
        return fileUploadResponse;
    }

    private FileUploadResponse initialFileUpload(String fileName) throws IOException {
            UUID uuid = UUID.randomUUID();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            FileUploadResponse fileUploadResponse =new FileUploadResponse(uuid.toString(),fileName,timestamp);
            return fileUploadResponse;
    }

    private Resource getFileResource(String fileName) throws IOException {
        try {
            Path uploadPath = Paths.get(uploadDirectory);
            Resource resource = new UrlResource(uploadPath.resolve(fileName).toFile().toURI());
            if (resource.exists() && resource.isReadable())
                return resource;
            else
                throw new FileNotFoundException("File not found");

        } catch (MalformedURLException malformedURLException) {
            throw new FileSaveException("Unable to create resource as url is malformed for file name : " + fileName);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new FileSaveException("Unable to create resource, file not found! for file name " + fileName);
        }
    }

    public void deleteFiles(String deletionFiles) {
            LOGGER.info("Starting file deletion ");
            if(!deletionFiles.isEmpty())
            deleteFile(deletionFiles);
            LOGGER.info("File deletion complete");
        }

    public void deleteFile(String fileName) {
        try {
            Files.delete(Paths.get(uploadDirectory).resolve(fileName));
        } catch (IOException ex) {
            LOGGER.warn("Unable to delete file file-name {}", fileName);
        }
    }
    Path saveFile(String fileName, MultipartFile multipartFile, String uPath) throws IOException {
        Path uploadPath = Paths.get(uPath);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            System.out.println(filePath.toAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return uploadPath;
        } catch (IOException exception) {
            throw new IOException("Could Not save file ");
        }
    }
}
