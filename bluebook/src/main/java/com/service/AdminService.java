package com.service;

import com.Dao.BlogDao;
import com.Dao.UserDao;
import com.pojo.Blog;
import com.pojo.User;
import com.util.Constant;
import com.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserDao usersDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private RedisOperator redisOperator;

//    @Autowired
//    private EsService esService;

    @Transactional(propagation = Propagation.REQUIRED)
    //删缓存存货
    public void deleteBlog(Long id) {
        redisOperator.lremove(Constant.PAGE_BLOG, 0, redisOperator.hget(Constant.BLOG_DETAIL, String.valueOf(id)));
        redisOperator.del(Constant.BLOG_DETAIL+id);
        redisOperator.hdel(Constant.BLOG_DETAIL, String.valueOf(id));
        // esService.removeEsBlog(id); // 搜索
        // Blog byId = adminMapper.findById(id);
        blogDao.deleteById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUsers(String username) {
        User user=new User();
        user=usersDao.findByUsername(username);
        usersDao.delete(user);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Blog> findAllBlogs(Integer pageSize, Integer pageNum) {
        Pageable pageable= PageRequest.of(pageNum, pageSize);
        Page<Blog> blogPage=blogDao.findBlog(pageable);
        List<Blog> blogList=blogPage.getContent();
        return blogList;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> findAllUsers(Integer pageSize, Integer pageNum) {
        Pageable pageable= PageRequest.of(pageNum, pageSize);
        Page<User> userPage=usersDao.findAll(pageable);
        List<User> userList=userPage.getContent();
        return userList;
    }


}
