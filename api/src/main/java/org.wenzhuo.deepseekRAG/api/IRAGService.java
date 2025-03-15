package org.wenzhuo.deepseekRAG.api;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.wenzhuo.deepseekRAG.api.reponse.Response;

import java.util.List;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/15
 * @description:
 */
public interface IRAGService {
    public Response<List<String>> queryRagTagList();

    public Response<String> uploadFile(@RequestParam String ragTag, @RequestParam("file") List<MultipartFile> files);
}
