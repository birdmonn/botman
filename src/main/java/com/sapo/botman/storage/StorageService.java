package com.sapo.botman.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file, String stringPath);

    Stream<Path> loadAll();

    Path load(String pathId, String filename);

    Resource loadAsResource(String pathId, String filename);

    void deleteFileByPath(String path);

    void deleteAll();

}
