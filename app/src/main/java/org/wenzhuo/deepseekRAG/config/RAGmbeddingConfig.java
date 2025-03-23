package org.wenzhuo.deepseekRAG.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/9
 * @description:
 */
@Configuration
@Slf4j
public class RAGmbeddingConfig {

    /**
     *  @author:wenzhuo4657
        des: 这是一个使用 CL100K_BASE 编码，根据标记数将文本分割成块TokenTextSplitter的实现
    */
    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return new TokenTextSplitter();
    }


    /**
     *  @author:wenzhuo4657
    des: PostgreSQL/PGVector向量存储
     */
    @Bean
    public PgVectorStore pgVectorStore(EmbeddingModel embeddingModel, JdbcTemplate jdbcTemplate) {
        return PgVectorStore.builder(jdbcTemplate,embeddingModel).vectorTableName("vectorstore").build();
    }

    /**
     *  @author:wenzhuo4657
        des: PGVector的文档检索
    */
    @Bean
    public VectorStoreDocumentRetriever documentRetriever(PgVectorStore pgVectorStore){
        return VectorStoreDocumentRetriever.builder().vectorStore(pgVectorStore)
                .build();
    }


    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    /**
     *  @author:wenzhuo4657
        des: 文档上传
    */
    @Bean
    CommandLineRunner ingestTermOfServiceToVectorStore(EmbeddingModel embeddingModel, VectorStore vectorStore,
                                                       @Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {

        return args -> {
            // Ingest the document into the vector store
            vectorStore.write(new TokenTextSplitter().transform(new TextReader(termsOfServiceDocs).read()));

            vectorStore.similaritySearch("Cancelling Bookings").forEach(doc -> {
                log.info("Similar Document: {}", doc.getContent());
            });
        };
    }




}
