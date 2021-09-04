package com.controller;

import com.pojo.Collection;
import com.pojo.Focus;
import com.pojo.User;
import com.result.Result;
import com.result.ResultFactory;
import com.service.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
public class FocusController {
    @Autowired
    FocusService focusService;

    //关注用户
    @GetMapping("/getUserFocus")
    public Result getUserFocus(@NotBlank String username,@NotBlank String fans){
        boolean sign = focusService.getIsFocus(username,fans);
        if (sign){
            focusService.deleteUserFocus(username,fans);
            return ResultFactory.buildSuccessResult("取消关注成功",null);
        }else {
            focusService.getUserFocus(username,fans);
            return ResultFactory.buildSuccessResult("关注成功",null);
        }
    }

    //获得个人关注列表
    @GetMapping("/getFocusList")
    public Result getFocusList(@NotBlank String username){
        List<User> focusList=focusService.getFocusListByUsername(username);
        return ResultFactory.buildSuccessResult("", focusList);
    }

    //获得粉丝列表
    @GetMapping("/getFansList")
    public Result getFansList(@NotBlank String username){
        List<User> fansList=focusService.getFansListByUsername(username);
        return ResultFactory.buildSuccessResult("", fansList);
    }

    //获取关注情况
    @GetMapping("/getIsFocus")
    public Result getIsFocus(@NotBlank String username,@NotBlank String fans){
        Boolean sign=false;
        sign=focusService.getIsFocus(username,fans);
        return ResultFactory.buildSuccessResult("",sign);
    }

}
