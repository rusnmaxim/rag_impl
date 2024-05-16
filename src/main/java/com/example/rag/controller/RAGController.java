package com.example.rag.controller;

import com.example.rag.domain.dto.AnswerDTO;
import com.example.rag.service.RAGService;
import com.example.rag.service.RAGServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag")
@RequiredArgsConstructor
public class RAGController {
    private final RAGService ragServiceImpl;

    @GetMapping
    public ResponseEntity<AnswerDTO> completion(@RequestParam(value = "question") String question,
                                                @RequestParam(value = "useDocument", defaultValue = "true") boolean useDocument) {
        return ResponseEntity.ok(this.ragServiceImpl.generate(question, useDocument));
    }
}
