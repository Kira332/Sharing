package com.controller;

import com.Dao.BlogDao;
import com.pojo.Blog;
import com.result.Result;
import com.result.ResultFactory;
import com.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Blob;
import java.util.List;

@RestController
public class EditorController {

    @Autowired
    BlogService blogService;

    @Autowired
    BlogDao blogDao;
    //发布笔记
    @PostMapping("publishEditor")
    public Result publishEditor(@Valid Blog blogMessage) {
        Long id = blogMessage.getId();
        //修改文章
        if (id != null) {
            return ResultFactory.buildSuccessResult( "修改成功", blogService.save(blogMessage));
        }

        blogService.save(blogMessage);
        return ResultFactory.buildSuccessResult("",blogMessage);
    }


    @GetMapping("/getDraft")
    public Result getDraft(String username){
        List<Blog> drafts=blogService.findAllDraftByUsername(username);
        return ResultFactory.buildSuccessResult("", drafts);
    }
}
