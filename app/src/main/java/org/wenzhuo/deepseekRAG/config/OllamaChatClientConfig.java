package org.wenzhuo.deepseekRAG.config;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/9
 * @description:
 */
@Configuration
public class OllamaChatClientConfig {

    @Value("${spring.ai.ollama.base-url}")
    public  String baseUrl;
    @Bean
    public OllamaChatClient ollamaChatClient(){
        return  new OllamaChatClient(new OllamaApi(baseUrl));
    }
}
