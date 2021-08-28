package com.service;

import com.Dao.RegisterDao;
import com.Dao.UserDao;
import com.pojo.User;
import com.util.RSA;
import com.util.RSAUtils;
import com.util.ShiroMD5;
import com.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Service
public class RegisterService {
    @Autowired
    private RegisterDao registerDao;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private RedisService redisService;

    RSA rsa = new RSA();

    @Transactional(propagation = Propagation.REQUIRED)
    public void registerUser(User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setRoleId(2);
        // 后端获取私钥
        String privateKey = rsa.privateKey;
        // 获得RSA类型的私钥
        RSAPrivateKey rsaPrivateKey = RSAUtils.getPrivateKey(privateKey);
        // 使用私钥解密经过前端加密用户输入的密文
        String Decrypt_web = RSAUtils.privateDecrypt(user.getPassword(), rsaPrivateKey);
        Object o = ShiroMD5.MD5(user.getPhone(), Decrypt_web);
        user.setPassword(String.valueOf(o));
        String data = new TimeUtil().getFormatDateForThree();
        user.setLastTime(data);
        // 手机号和用户名存入缓存
        redisService.SavePhoneAndUsername(user.getPhone(), user.getUsername());
        registerDao.save(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public int findByPhone(String phone) {
        if (registerDao.findAllByPhone(phone).isEmpty()){
            asyncService.insUserPhone();
            return 1;
        }else {
            return 0;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public int findByUsername(String username) {
        if (registerDao.findAllByUsername(username).isEmpty()){
            asyncService.insUserName();
            return 1;
        }else {
            return 0;
        }
    }
}
