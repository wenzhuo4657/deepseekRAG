package org.wenzhuo.deepseekRAG.config;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/9
 * @description:
 */
@Configuration
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
        return PgVectorStore.builder(jdbcTemplate,embeddingModel).vectorTableName("VectorStore").build();
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





}
