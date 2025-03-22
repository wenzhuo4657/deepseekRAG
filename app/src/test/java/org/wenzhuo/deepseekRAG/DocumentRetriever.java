package org.wenzhuo.deepseekRAG;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/22
 * @description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DocumentRetriever {

    @Autowired
    private VectorStoreDocumentRetriever documentRetriever;


    @Test
    public  void  test(){

        List<Document> documentList = documentRetriever.retrieve(new Query("What's spring ai"));
    }
}
