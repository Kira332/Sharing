package com.controller;

import com.pojo.Type;
import com.result.Result;
import com.result.ResultFactory;
import com.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagController {

    @Autowired
    TypeService typeService;

    @GetMapping("/allTags")
    public Result getAllTags(){
        List<Type> typeList=typeService.findAllTags();
        return ResultFactory.buildSuccessResult("", typeList);
    }
}
