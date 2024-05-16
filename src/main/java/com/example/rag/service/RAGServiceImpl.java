package com.example.rag.service;

import com.example.rag.domain.dto.AnswerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGServiceImpl implements RAGService{

    @Value("classpath:/prompts/system-qa.st")
    private Resource qaSystemPromptResource;

    @Value("classpath:/prompts/system-chatbot.st")
    private Resource chatbotSystemPromptResource;

    private final ChatClient chatClient;
    private final VectorStore vectorStoreRetriever;


    public AnswerDTO generate(String message, boolean useDocument) {
        Message systemMessage = getSystemMessage(message, useDocument);
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse aiResponse = chatClient.call(prompt);

        return new AnswerDTO(message, aiResponse.getResult().getOutput().getContent()) ;
    }

    private Message getSystemMessage(String message, boolean stuffit) {
        if (stuffit) {
            List<Document> similarDocuments = vectorStoreRetriever.similaritySearch(message);
            String documents = similarDocuments.stream().map(entry -> entry.getContent()).collect(Collectors.joining("\n"));
            SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(this.qaSystemPromptResource);
            return systemPromptTemplate.createMessage(Map.of("documents", documents));
        } else {
            return new SystemPromptTemplate(this.chatbotSystemPromptResource).createMessage();
        }
    }
}
