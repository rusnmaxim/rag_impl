package com.example.rag.service;

import com.example.rag.domain.dto.AnswerDTO;

public interface RAGService {
    AnswerDTO generate(String message, boolean useDocument);

}
