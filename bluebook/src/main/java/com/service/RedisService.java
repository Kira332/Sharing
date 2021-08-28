package com.service;

import com.pojo.Blog;
import com.util.Constant;
import com.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: qiang
 * @ProjectName: adminsystem
 * @Package: com.qiang.modules.sys.service.impl
 * @Description:
 * @Date: 2019/8/17 0017 12:16
 **/
@Service
public class RedisService {

    @Autowired
    private RedisOperator redisOperator;

    @Transactional(propagation = Propagation.REQUIRED)
    public void SavePhoneAndUsername(String phone, String username) {
        redisOperator.hset(Constant.USER_PHONE_EXIST, phone, 1);
        redisOperator.hset(Constant.USER_NAME_EXIST, username, 1);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void SaveEditBlog(Blog blog) {
        redisOperator.lpush(Constant.PAGE_BLOG, blog);
        redisOperator.hset(Constant.BLOG_DETAIL, String.valueOf(blog.getId()), blog);
        redisOperator.set(Constant.BLOG_DETAIL + blog.getId(), 0);
    }
    
}
