package com.example.rag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileLoadServiceImpl implements FileLoadService {

    @Value("classpath:/data/medicaid-wa-faqs.pdf")
    private Resource pdfResource;

    private final JdbcTemplate jdbcTemplate;
    private final VectorStore vectorStore;


    public void load() {
        PagePdfDocumentReader  pdfReader = new PagePdfDocumentReader(
                this.pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(3)
                                .withNumberOfTopPagesToSkipBeforeDelete(1)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        var textSplitter = new TokenTextSplitter();

        this.vectorStore.accept(
                textSplitter.apply(
                        pdfReader.get()));

    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM vector_store";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void delete(){
        String sql = "DELETE FROM vector_store";
        jdbcTemplate.update(sql);
    }
}
