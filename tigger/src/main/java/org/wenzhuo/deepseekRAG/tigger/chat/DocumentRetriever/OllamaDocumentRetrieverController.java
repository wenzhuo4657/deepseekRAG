package org.wenzhuo.deepseekRAG.tigger.chat.DocumentRetriever;

import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/22
 * @description:
 */
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ollama/chat/DocumentRetriever")
public class OllamaDocumentRetrieverController {
    private final DocumentRetriever documentRetriever;

    @Autowired
    public OllamaDocumentRetrieverController(DocumentRetriever documentRetriever) {
        this.documentRetriever = documentRetriever;
    }


}
