package org.wenzhuo.deepseekRAG;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.metadata.OllamaEmbeddingUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/21
 * @description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EmbeddingModelTest {
    @Autowired
    private  EmbeddingModel embeddingModel;




    @Test
    public  void embed(){

//        这个相似计算如何使用矢量数据库？如何使用？
        EmbeddingResponse embed = this.embeddingModel.call(new EmbeddingRequest(List.of("这是什么","你好？"),
                OllamaOptions.builder()
                        .model("deepseek-r1:1.5b").build()));
        Map<String, EmbeddingResponse> embedding = Map.of("embedding", embed);
        for (Map.Entry<String,EmbeddingResponse> entry:embedding.entrySet()){
            String key = entry.getKey();
            EmbeddingResponse value = entry.getValue();
            System.out.println();
        }
        System.out.println();
    }

}
