package org.wenzhuo.deepseekRAG;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/9
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RAGtest {

    @Resource
    private OllamaChatClient chatClient;
    @Resource
    private TokenTextSplitter tokenTextSplitter;
    @Resource
    private SimpleVectorStore simpleVectorStore;
    @Resource
    private PgVectorStore pgVectorStore;

    @Test
    public void upload() {
        TikaDocumentReader reader = new TikaDocumentReader("./data/test.md");
        List<Document> documents = reader.get();
        List<Document> documentSplitterList = tokenTextSplitter.apply(documents);

        documents.forEach(doc -> doc.getMetadata().put("knowledge", "ai-rag-knowledge"));

        documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge", "ai-rag-knowledge"));

        pgVectorStore.accept(documentSplitterList);
        log.info("上传完成");
    }

    @Test
    public  void chat(){
        String message = "李二，什么时候出生";
        String SYSTEM_PROMPT = """
            Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
            If unsure, simply state that you don't know.
            Another thing you need to note is that your reply must be in Chinese!
            DOCUMENTS:
                {documents}
            """;
        SearchRequest request = SearchRequest.query(message)
                .withTopK(5)
                .withFilterExpression("knowledge == 'ai-rag-knowledge'");
        List<Document> documents = pgVectorStore.similaritySearch(request);
        String documentCollectors = documents.stream().map(Document::getContent).collect(Collectors.joining());
        Message ragMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(Map.of("documents", documentCollectors));
        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        messages.add(ragMessage);
        ChatResponse chatResponse = chatClient.call(
                new Prompt(
                        messages,
                        OllamaOptions.create()
                                .withModel("deepseek-r1:1.5b")
                ));
        log.info("测试结果：{}", JSON.toJSONString(chatResponse));
    }

    /**
     *  @author:wenzhuo4657
        des: simpleVectorStore测试
    */
    @Test
    public  void test(){
        TikaDocumentReader reader = new TikaDocumentReader("./data/test.md");
        List<Document> documents = reader.get();
        List<Document> documentSplitterList = tokenTextSplitter.apply(documents);

        documents.forEach(doc -> doc.getMetadata().put("knowledge", "myBook"));

        documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge", "myBook"));

        simpleVectorStore.accept(documentSplitterList);
        log.info("上传完成");


        String message = "王大瓜，什么时候出生";
        String SYSTEM_PROMPT = """
            Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
            If unsure, simply state that you don't know.
            Another thing you need to note is that your reply must be in Chinese!
            DOCUMENTS:
                {documents}
            """;
        SearchRequest request = SearchRequest.query(message)
                .withTopK(5);
//                .withFilterExpression("knowledge == 'mybook'");
        List<Document> documents1 = simpleVectorStore.similaritySearch(request);
        String documentCollectors = documents1.stream().map(Document::getContent).collect(Collectors.joining());
        Message ragMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(Map.of("documents", documentCollectors));
        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        messages.add(ragMessage);
        ChatResponse chatResponse = chatClient.call(
                new Prompt(
                        messages,
                        OllamaOptions.create()
                                .withModel("deepseek-r1:1.5b")
                ));
        log.info("测试结果：{}", JSON.toJSONString(chatResponse));
    }


}
