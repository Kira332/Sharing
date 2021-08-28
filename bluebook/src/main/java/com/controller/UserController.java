package com.controller;

import com.pojo.*;
import com.result.Result;
import com.result.ResultFactory;
import com.service.UserService;
import com.util.RSA;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    //用户退出
    @GetMapping("logout")
    public Result logout(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        if(subject != null){
            request.getSession().invalidate();
            subject.logout();
        }
        return ResultFactory.buildSuccessResult(null, null);
    }

    //用户是否过期
    @GetMapping("/isLogin")
    public Result isLogin(HttpServletRequest request){
        return ResultFactory.buildSuccessResult(null, null);
    }

    //获取个人信息
    @GetMapping("getUserMess")
    public Result getUserMess( String username){
        User userMess = userService.findUserMess(username);
        return ResultFactory.buildSuccessResult(null, userMess);
    }

    //新增个人信息
    @PostMapping("/changeUserMess")
    public Result changeUserMess(@RequestBody @Valid User giveUser){
        User user= userService.changeUserMess(giveUser);
        if(user != null){
            return ResultFactory.buildSuccessResult(null, user);
        }else{
            return ResultFactory.buildFailResult("修改失败");
        }
    }


    //我的评论
    @GetMapping("getComment")
    public Result getComment(String username){
        List<Comment> commentList=userService.findComments(username);
        return ResultFactory.buildSuccessResult(null,commentList);
    }

    //新收的评论消息已读
    @GetMapping("clearComment")
    public Result clearComment(String username){
        userService.clearComment(username);
        return ResultFactory.buildSuccessResult();
    }

    //新增的点赞列表已读
    @GetMapping("clearLikes")
    public Result clearLikes(String username){
        userService.clearLikes(username);
        return ResultFactory.buildSuccessResult();
    }

}
