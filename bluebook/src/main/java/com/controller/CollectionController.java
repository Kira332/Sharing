package com.controller;

import com.pojo.Blog;
import com.result.Result;
import com.result.ResultFactory;
import com.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CollectionController {
    @Autowired
    CollectionService collectionService;

    //返回收藏关系
    @GetMapping("/getIsCollection")
    public Result getIsCollection(String username, Long blogId){
        return ResultFactory.buildSuccessResult("",collectionService.getIsCollection(username,blogId));
    }


    //我的收藏
    @GetMapping("getUserCollection")
    public Result getUserCollection(String username){
        List<Blog> collection = collectionService.findCollections(username);
        return ResultFactory.buildSuccessResult(null,collection);
    }

}
