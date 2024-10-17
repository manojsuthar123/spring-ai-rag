package com.get.details;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Controller {

    private final OllamaChatModel chatClient;
    private final EmbeddingDocument embeddingDocument;
    private final VectorStore vectorStore;

    public Controller(OllamaChatModel chatClient, EmbeddingDocument embeddingDocument, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.embeddingDocument = embeddingDocument;
        this.vectorStore = vectorStore;
    }


    @GetMapping("/ai/train/embedding")
    public Map<String, Object> embed(@RequestParam String filename) throws IOException {
        embeddingDocument.embedDocuments(filename);
        return Map.of("status", "SUCCESS");
    }

    @GetMapping("/ai")
    Map<String, String> generation(@RequestParam String message) {
        List<Document> listOfSimilarDocuments = vectorStore.similaritySearch(message);

        String documentsText = listOfSimilarDocuments
                .stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

        var systemMessage = new SystemPromptTemplate(Constants.DEFAULT_USER_TEXT_ADVISE)
                .createMessage(Map.of("document_text", documentsText));
        var userMessage = new UserMessage(message);
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse call = this.chatClient.call(prompt);
        return Map.of("response", call.getResult().getOutput().getContent());
    }
}
