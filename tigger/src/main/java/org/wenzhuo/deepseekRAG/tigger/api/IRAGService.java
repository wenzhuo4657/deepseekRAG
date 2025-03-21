package org.wenzhuo.deepseekRAG.tigger.api;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.wenzhuo.deepseekRAG.tigger.api.reponse.Response;

import java.util.List;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/15
 * @description:
 */
public interface IRAGService {

    /**
     *  @author:wenzhuo4657
        des: 查询知识库列表
    */
    public Response<List<String>> queryRagTagList();

    /**
     *  @author:wenzhuo4657
        des: 上传文件到知识库ragTag
    */
    public Response<String> uploadFile(@RequestParam String ragTag, @RequestParam("file") List<MultipartFile> files);


    /**
     *  @author:wenzhuo4657
        des:  解析git仓库
    repoUrl:不能用ssh,要用https
    */
    public Response<String> analyzeGitRepository(@RequestParam String repoUrl, @RequestParam String userName, @RequestParam String token) throws Exception;
}
