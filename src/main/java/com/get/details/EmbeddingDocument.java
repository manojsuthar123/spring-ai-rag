package com.get.details;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class EmbeddingDocument {

    private final VectorStore vectorStore;
    private final ResourceLoader resourceLoader;

    public EmbeddingDocument(VectorStore vectorStore, ResourceLoader resourceLoader) {
        this.vectorStore = vectorStore;
        this.resourceLoader = resourceLoader;
    }

    public void embedDocuments() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:filescad.pdf");
        Path path = Paths.get(resource.getURI());
        TikaDocumentReader documentReader = new TikaDocumentReader(path.toUri().toString());
        List<Document> documents = documentReader.get();
        List<Document> splitDocuments = new TokenTextSplitter().apply(documents);
        vectorStore.add(splitDocuments);
    }
}
