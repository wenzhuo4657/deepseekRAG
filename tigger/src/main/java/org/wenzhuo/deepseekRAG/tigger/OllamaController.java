package org.wenzhuo.deepseekRAG.tigger;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.wenzhuo.deepseekRAG.api.IAiService;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/9
 * @description:
 */
    @RestController()
    @CrossOrigin("*")
    @RequestMapping("/api/v1/ollama/")
public class OllamaController implements IAiService {

    private final ChatClient chatClient;

    private final ChatModel ollamaiChatModel;
    @Resource
    private PgVectorStore pgVectorStore;


    public OllamaController(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.ollamaiChatModel = chatModel;
        // 构造时，可以设置 ChatClient 的参数
        // {@link org.springframework.ai.chat.client.ChatClient};
        this.chatClient = ChatClient.builder(chatModel)
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                // 实现 Logger 的 Advisor
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
//                这里的模型是无效的，需要被覆盖
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        OllamaOptions.builder()
                                .topP(0.7)
                                .model("llam3")
                                .build()
                )
                .build();
    }

    /**
     * curl http://localhost:8090/api/v1/ollama/generate?model=deepseek-r1:1.5b&message=1+1
     */
    @RequestMapping(value = "generate", method = RequestMethod.GET)
    @Override
    public ChatResponse generate(@RequestParam String model, @RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .options(ChatOptions.builder().model(model).build())
                .call()
                .chatResponse();
    }

    /**
     * curl http://localhost:8090/api/v1/ollama/generate_stream?model=deepseek-r1:1.5b&message=1+1
     */
    @RequestMapping(value = "generate_stream", method = RequestMethod.GET)
    @Override
    public Flux<ChatResponse> generateStream(@RequestParam String model, @RequestParam String message) {
        return  chatClient.prompt().user(message)
                .stream()
                .chatResponse();
    }

    @RequestMapping(value = "generate_stream_rag", method = RequestMethod.GET)
    @Override
    public Flux<ChatResponse> generateStreamRag(@RequestParam String model, @RequestParam String ragTag, @RequestParam String message) {

        String SYSTEM_PROMPT = """
                Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
                If unsure, simply state that you don't know.
                Another thing you need to note is that your reply must be in Chinese!
                DOCUMENTS:
                    {documents}
                """;

        // 指定文档搜索
        SearchRequest request = SearchRequest.builder().query(message)
                .topK(5)
                .filterExpression("knowledge == '" + ragTag + "'").build();

        List<Document> documents = pgVectorStore.similaritySearch(request);

        String documentCollectors = documents.stream().map(Document::getContent).collect(Collectors.joining());
        Message ragMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(Map.of("documents", documentCollectors));

        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        messages.add(ragMessage);


        return  chatClient.prompt()
                .messages(messages)
                .stream()
                .chatResponse();


    }
}
