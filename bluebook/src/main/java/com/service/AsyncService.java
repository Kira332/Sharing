package com.service;

import com.Dao.BlogDao;
import com.Dao.RegisterDao;
import com.pojo.Blog;
import com.pojo.User;
import com.util.Constant;
import com.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AsyncService {

        @Autowired
        private RedisOperator redisOperator;

        @Autowired
        private RegisterDao registerDao;

        @Autowired
        private BlogDao blogDao;

        @Async
        @Transactional(propagation = Propagation.REQUIRED)
        public void updBlogLook(Long id, Long view) {
            Blog blog = new Blog();
            blog.setView(view);
            blog.setId(id);
            blogDao.save(blog);
        }

        @Async
        @Transactional(propagation = Propagation.SUPPORTS)
        public void insUserPhone() {
            List<User> allUsers = registerDao.findAll();
            for (User u:
                    allUsers) {
                redisOperator.hset(Constant.USER_PHONE_EXIST, u.getPhone(), 1);
            }
        }

        @Async
        @Transactional(propagation = Propagation.REQUIRED)
        public void insUserName() {
            List<User> allUsers = registerDao.findAll();
            for (User u:
                    allUsers) {
                redisOperator.hset(Constant.USER_NAME_EXIST, u.getUsername(), 1);
            }
        }

}
