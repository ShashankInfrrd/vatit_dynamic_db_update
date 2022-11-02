package ai.infrrd.vatit_dynamic_db_update.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public interface FileConversionService {
    default Path saveFile(String fileName, MultipartFile multipartFile, String uPath) throws IOException {
        Path uploadPath = Paths.get(uPath);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return uploadPath;
        } catch (IOException exception) {
            throw new IOException("Could Not save file ");
        }
    }
}
