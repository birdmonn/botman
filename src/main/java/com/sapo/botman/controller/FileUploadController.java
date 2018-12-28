package com.sapo.botman.controller;

import com.sapo.botman.model.QuestPokemonGo;
import com.sapo.botman.service.QuestPokemonGoService;
import com.sapo.botman.storage.StorageFileNotFoundException;
import com.sapo.botman.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private QuestPokemonGoService questPokemonGoService;

    @Autowired
    public FileUploadController(StorageService storageService,
                                QuestPokemonGoService questPokemonGoService) {
        this.storageService = storageService;
        this.questPokemonGoService = questPokemonGoService;
    }

    @GetMapping("/downloads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
//        String originalFileName = questPokemonGoService.findAll().get(0).getUrl();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"quest.jpg\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
