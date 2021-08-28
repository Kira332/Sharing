package com.service;

import com.Dao.BlogDao;
import com.Dao.LikeDao;
import com.pojo.Blog;
import com.pojo.Collection;
import com.pojo.Like;
import com.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    LikeDao likeDao;

    @Autowired
    BlogDao blogDao;

    public boolean isLike(Like like) {
        return likeDao.existsByBlogIdAndLname(like.getBlogId(), like.getLname());
    }

    public void delete(Like like){
        Like like1=likeDao.findByBlogIdAndLname(like.getBlogId(), like.getLname());
        likeDao.delete(like1);
    }

    public void add(Like like){
        likeDao.save(like);
    }

    public Boolean getWhetherLike(String username,Long blogId){
        return likeDao.existsByBlogIdAndLname(blogId, username);
    }

    public List<Blog> findLikes(String username){
        List<Like> likeList=likeDao.findAllByUsername(username);
        List<Long> blogIdList=likeList.stream().map(Like::getBlogId).collect(Collectors.toList());
        List<Blog> blogList=new ArrayList<>();
        for (int i=0;i<blogIdList.size();i++){
            long blogId=blogIdList.get(i);
            Blog collector=blogDao.findById(blogId);
            blogList.add(collector);
        }
        return blogList;
    }
}
