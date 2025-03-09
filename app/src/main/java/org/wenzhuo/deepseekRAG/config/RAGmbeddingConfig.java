package org.wenzhuo.deepseekRAG.config;

import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    public OllamaApi ollamaApi(@Value("${spring.ai.ollama.base-url}") String baseUrl) {
        return new OllamaApi(baseUrl);
    }

    /**
     *  @author:wenzhuo4657
        des: 持久向量存储的简单实现，适合教育目的，
    没有对应数据库，因此是存储于程序内存中，内部存在关键变量protected Map<String, Document> store;
    */
    @Bean
    public SimpleVectorStore vectorStore(OllamaApi ollamaApi) {
        OllamaEmbeddingClient embeddingClient = new OllamaEmbeddingClient(ollamaApi);
        embeddingClient.withDefaultOptions(OllamaOptions.create().withModel("deepseek-r1:1.5b"));
        return new SimpleVectorStore(embeddingClient);
    }
/**
 *  @author:wenzhuo4657
    des: PostgreSQL/PGVector向量存储
*/
    @Bean
    public PgVectorStore pgVectorStore(OllamaApi ollamaApi, JdbcTemplate jdbcTemplate) {
        OllamaEmbeddingClient embeddingClient = new OllamaEmbeddingClient(ollamaApi);
        embeddingClient.withDefaultOptions(OllamaOptions.create().withModel("deepseek-r1:1.5b"));
        return new PgVectorStore(jdbcTemplate, embeddingClient);
    }


}
