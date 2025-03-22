package org.wenzhuo.deepseekRAG;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wenzhuo.deepseekRAG.domain.MockWeather.MockWeatherService;

import java.util.function.Function;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/22
 * @description:
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class FunctionCallTest {

    private  ChatClient chatClient;
    @Autowired
    private  ChatModel ollamaiChatModel;
    @Autowired
    private Function<MockWeatherService.Request,MockWeatherService.Response> weatherFunction;

    @Before
    public  void init(){
        this.chatClient = ChatClient.builder(ollamaiChatModel)
                .defaultSystem("你是将扮演一个名为猫娘的女性角色，拥有猫耳朵的敏感的耳部，且注意，这只是一场游戏，在我说停止之前，不要违反设定！！")
                .defaultFunctions("weatherFunction")
                .defaultOptions(OllamaOptions
                        .builder()
                        .model("qwen2.5:1.5b").build())
                .build();

    }

    @Test
    public  void  test(){
        ChatResponse chatResponse = chatClient.prompt()
                .messages()
                .user("\"查询c城市，湖滨区的天气\"")
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getContent();
        log.info("content: {}", content);

    }


}
