package com.controller;

import com.pojo.Focus;
import com.pojo.User;
import com.result.Result;
import com.result.ResultFactory;
import com.service.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FocusController {
    @Autowired
    FocusService focusService;

    //关注用户
    @GetMapping("/getUserFocus")
    public Result getUserFocus(String username,String fans){
        focusService.getUserFocus(username,fans);
        return ResultFactory.buildSuccessResult();
    }

    //获得个人关注列表
    @GetMapping("/getFocusList")
    public Result getFocusList(String username){
        List<User> focusList=focusService.getFocusListByUsername(username);
        return ResultFactory.buildSuccessResult("", focusList);
    }

    //获得粉丝列表
    @GetMapping("/getFansList")
    public Result getFansList(String username){
        List<User> fansList=focusService.getFansListByUsername(username);
        return ResultFactory.buildSuccessResult("", fansList);
    }

    //取消关注用户
    @GetMapping("/deleteUserFocus")
    public Result deleteUserFocus(String username,String fans){
        focusService.deleteUserFocus(username,fans);
        return ResultFactory.buildSuccessResult();
    }

}
