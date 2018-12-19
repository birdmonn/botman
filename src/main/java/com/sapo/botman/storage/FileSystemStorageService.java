package com.sapo.botman.storage;

import com.sapo.botman.utils.MessageUtils;
import com.sapo.botman.utils.SplitString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final StorageProperties properties;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.properties = properties;
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(InputStream file, String stringPath) {
        String fileName = "downloadsquestssssssss.jpg";
        try {
            if (fileName.contains("..")) {
                // This is a security check
                throw new StorageException(MessageUtils.CANNOT_STORE_FILE
                        + fileName);
            }
            Path customPath = Paths.get(properties.getLocation());
            Files.createDirectories(customPath);
            Files.copy(file, customPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("in store");
            return "/" + properties.getLocationReport() + stringPath + fileName;
        } catch (IOException e) {
            throw new StorageException(MessageUtils.FAILED_TO_STORE_FILE + fileName, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        Path customPath = Paths.get(properties.getLocation());
        return customPath.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void deleteFileByPath(String path) {
        Path customPath = Paths.get(properties.getLocationReport() + SplitString.getInstance().stringPathReport(path));
        FileSystemUtils.deleteRecursively(customPath.toFile());
    }
}
