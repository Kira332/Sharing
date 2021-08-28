package com.controller;

import com.pojo.User;
import com.result.Result;
import com.result.ResultFactory;
import com.service.UserService;
import com.util.RSA;
import com.util.RSAUtils;
import com.util.TokenUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@RestController
public class LoginController {

    RSA rsa = new RSA();

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String defaultIndex(HttpSession session) {
        session.setAttribute("publicKey", rsa.publicKey);
        return "index";
    }

    @PostMapping("loginUser")
    public Result loginUser(@RequestParam("phone") String phone,
                            @RequestParam("password") String password
                            )throws InvalidKeySpecException, NoSuchAlgorithmException{
        // 后端获取私钥
        String privateKey = rsa.privateKey;
        // 获得RSA类型的私钥
        RSAPrivateKey rsaPrivateKey = RSAUtils.getPrivateKey(privateKey);

        // 使用私钥解密经过前端加密用户输入的密文
        String Decrypt_web = RSAUtils.privateDecrypt(password, rsaPrivateKey);

        //TODO 验证码验证
        UsernamePasswordToken token = new UsernamePasswordToken(phone, Decrypt_web);
        SecurityUtils.getSubject().login(token);

        //设置session时间
        SecurityUtils.getSubject().getSession().setTimeout(1000*60*30);

        //token信息
        Subject subject = SecurityUtils.getSubject();
        Serializable tokenId = subject.getSession().getId();

        String username=userService.findUserByPhone(phone).getUsername();

        TokenUtils tokenUtils=new TokenUtils();
        tokenUtils.setTokenId(tokenId);
        tokenUtils.setUsername(username);

        return ResultFactory.buildSuccessResult( "登录认证成功", tokenUtils);

    }
}
