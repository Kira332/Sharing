package com.controller;

import com.pojo.User;
import com.result.Result;
import com.result.ResultFactory;
import com.service.RegisterService;
import com.service.SMSService;
import com.util.Constant;
import com.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @Autowired
    RedisOperator redisOperator;

    @Autowired
    SMSService smsService;

    //注册
    @PostMapping("register")
    public Result register(@RequestBody @Valid  User users) throws InvalidKeySpecException, NoSuchAlgorithmException {
        registerService.registerUser(users);
        return ResultFactory.buildSuccessResult();
    }



    //手机号检测
    @PostMapping("phoneCheck")
    public Result phoneCheck(String phone){
        // 先从缓存中查询
        if(redisOperator.hsize(Constant.USER_PHONE_EXIST) != 0){
            if(!redisOperator.hasHkey(Constant.USER_PHONE_EXIST, phone)){
                return ResultFactory.buildSuccessResult();
            }else{
                return ResultFactory.buildFailResult("该手机号已被注册");
            }
        }else{
            int result = registerService.findByPhone(phone);
            if(result == 0){
                return ResultFactory.buildSuccessResult();
            }else{
                return ResultFactory.buildFailResult("该手机号已被注册");
            }
        }
    }


    //用户检测
    @GetMapping("usernameCheck")
    public Result usernameCheck( String username){
        // 先从缓存中查询
        if(redisOperator.hsize(Constant.USER_NAME_EXIST) != 0){
            if(!redisOperator.hasHkey(Constant.USER_NAME_EXIST, username)){
                return ResultFactory.buildSuccessResult();
            }else{
                return ResultFactory.buildFailResult("该用户已存在");
            }

        }else{
            int result = registerService.findByUsername(username);
            if(result == 0){
                return ResultFactory.buildSuccessResult();
            }else{
                return ResultFactory.buildFailResult("该用户已存在");
            }
        }
    }


    //获取验证码(点击按钮) -- 注册
    @PostMapping("getCode")
    public Result getCode( String phone){
        String s = smsService.sendMesModel(phone, 0);
        System.out.println(s);
        if(s.equals("OK")){
            return ResultFactory.buildSuccessResult();
        }else{
            return ResultFactory.buildFailResult("获取验证码失败");
        }
    }

    //获取验证码
    @PostMapping("getCodeReflush")
    public Result getCodeReflush(String phone){
        if(redisOperator.hasKey(Constant.USER_PHONE_CODE+phone)){
            return ResultFactory.buildSuccessResult(null,redisOperator.get(Constant.USER_PHONE_CODE+phone));
        }else{
            return ResultFactory.buildFailResult("验证码失效");
        }
    }
}
