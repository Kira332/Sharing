package com.controller;

import com.result.Result;
import com.result.ResultFactory;
import com.service.SMSService;
import com.service.UserService;
import com.util.Constant;
import com.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

/**
 * @Author: qiang
 * @ProjectName: adminsystem
 * @Package: com.qiang.modules.sys.controller
 * @Description: 忘记密码controller
 * @Date: 2019/8/4 0004 15:52
 **/
@RestController
public class FindPwdController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private SMSService smsService;

    /**
     *  获取验证码(点击按钮) -- 忘记密码
     * @param phone 手机号
     * @return
     */
    @GetMapping("getUpdPwdCode")
    public Result getUpdPwdCode(@RequestParam("phone") String phone){
        String s = smsService.sendMesModel(phone, 1);
        System.out.println(s);
        if(s.equals("OK")){
            return ResultFactory.buildSuccessResult();
        }else{
            return ResultFactory.buildFailResult("获取验证码失败");
        }
    }


    /**
     * 修改密码
     * @param password
     * @return
     */
    @GetMapping("findUsersPwd")
    @Transactional
    public Result findUsersPwd(@RequestParam("phone") String phone, @RequestParam("password") String password){
        userService.updUserPwd(phone, password);
        redisOperator.del(Constant.USER_PHONE_CODE+phone);
        return ResultFactory.buildSuccessResult();
    }

}
