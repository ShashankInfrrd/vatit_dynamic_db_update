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
import org.springframework.web.client.RestTemplate;
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
import java.util.function.BiPredicate;

@Service
public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    private static final String FILE_UPLOADED="file uploaded";

    @Value("${upload.path}")
    private String uploadDirectory;

//    @Value("${titan.post.url}")
//    private String titanUploadEndPoint;


//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private DynamicDbUpdation dynamicDbUpdation;


//    @LogExecutionTime
    public FileUploadResponse handleFile(MultipartFile multipartFile, String apiKey) throws IOException {
        FileUploadResponse fileUploadResponse;
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        saveFile(fileName,multipartFile,uploadDirectory);
        fileUploadResponse = initialFileUpload(fileName);
        Resource fileResource = getFileResource(fileName);
        System.out.println(dynamicDbUpdation.dynamicUpdation(fileUploadResponse,fileResource,fileName));
        deleteFiles(fileName);
        return fileUploadResponse;
    }

//    @LogExecutionTime
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

//    @LogExecutionTime
    private void deleteFiles(String deletionFiles) {
        LOGGER.info("Starting file deletion ");
        if(!deletionFiles.isEmpty())
        deleteFile(deletionFiles);
        LOGGER.info("File deletion complete");
    }

    private void deleteFile(String fileName) {
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
//        } catch (IOException exception) {
//            throw new IOException("Could Not save file ");
//        }
    } catch (Exception exception) {
//        throw new IOException("Could Not save file ");
            System.out.println(exception);
            throw new IOException("Could Not save file ");
    }
    }

}
