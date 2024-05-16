package com.example.rag.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AnswerDTO {
     String question;
     String answer;
}
