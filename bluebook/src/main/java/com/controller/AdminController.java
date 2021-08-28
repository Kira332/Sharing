package com.controller;

import com.pojo.Blog;
import com.pojo.User;
import com.result.Result;
import com.result.ResultFactory;
import com.service.AdminService;
import com.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    /**
     * 全部用户信息
     * @return
     */
    @GetMapping("getUsers")
    public Result getUserS(@RequestParam(value = "pageSize") Integer pageSize,
                           @RequestParam(value = "pageNum") Integer pageNum){
        Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin")){
            List<User> allUsers = adminService.findAllUsers(pageSize, pageNum);
            return ResultFactory.buildSuccessResult("",allUsers);
        }else{
            return ResultFactory.buildFailResult("无权限");
        }
    }

    /**
     * 全部博客信息
     * @return
     */
    @GetMapping("getBlogs")
    public Result getBlogs(@RequestParam(value = "pageSize") Integer pageSize,
                                   @RequestParam(value = "pageNum") Integer pageNum){
        Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin")){
            List<Blog> allBlogs = adminService.findAllBlogs(pageSize, pageNum);
            return ResultFactory.buildSuccessResult("",allBlogs);
        }else{
            return ResultFactory.buildFailResult("无权限");
        }
    }

    /**
     * 删除用户
     * @param username
     * @return
     */
    @GetMapping("delUsers")
    public Result delUsers(@RequestParam(value = "username") String username){
        Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin")){
            adminService.deleteUsers(username);
            return ResultFactory.buildSuccessResult();
        }else{
            return ResultFactory.buildFailResult("无权限");
        }
    }


    /**
     * 删除博客
     * @param id
     * @return
     */
    @GetMapping("delBlog")
    public Result delBlog(@RequestParam(value = "id") Long id){
        Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin")){
            adminService.deleteBlog(id);
            return ResultFactory.buildSuccessResult();
        }else{
            return ResultFactory.buildFailResult("无权限");
        }
    }

}

