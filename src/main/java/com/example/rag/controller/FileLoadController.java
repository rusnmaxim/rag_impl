package com.example.rag.controller;

import com.example.rag.service.FileLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class FileLoadController {
    private final FileLoadService fileLoadServiceImpl;

    @PostMapping("/load")
    public ResponseEntity<String> load() {
            this.fileLoadServiceImpl.load();
            return ResponseEntity.ok("Data loaded successfully!");
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> count() {
        return ResponseEntity.ok(fileLoadServiceImpl.count());
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete() {
      fileLoadServiceImpl.delete();
      return ResponseEntity.ok().build();
    }

}
