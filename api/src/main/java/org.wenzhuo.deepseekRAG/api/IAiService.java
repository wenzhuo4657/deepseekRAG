package org.wenzhuo.deepseekRAG.api;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/9
 * @description:
 */
public interface IAiService {
    public ChatResponse generate(String model, String message);
    public Flux<ChatResponse> generateStream(String model, String message);

    public Flux<ChatResponse> generateStreamRag(@RequestParam String model, @RequestParam String ragTag, @RequestParam String message);
}
