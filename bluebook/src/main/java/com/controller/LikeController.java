package com.controller;

import com.pojo.Blog;
import com.pojo.Like;
import com.result.Result;
import com.result.ResultFactory;
import com.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class LikeController {

    @Autowired
    LikeService likeService;

    @GetMapping("/getWhetherLike")
    public Result getWhetherLike(String username,Long blogId){
        Boolean sign=false;
        sign=likeService.getWhetherLike(username,blogId);
        return ResultFactory.buildSuccessResult("",sign);
    }

    //我的点赞
    @GetMapping("getUserLikes")
    public Result getUserLikes(String username){
        List<Blog> likes = likeService.findLikes(username);
        return ResultFactory.buildSuccessResult(null,likes);
    }

}
