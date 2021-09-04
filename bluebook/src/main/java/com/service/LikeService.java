package com.service;

import com.Dao.BlogDao;
import com.Dao.LikeDao;
import com.Dao.UserDao;
import com.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    LikeDao likeDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    UserDao userDao;

    public boolean isLike(Like like) {
        return likeDao.existsByBlogIdAndLname(like.getBlogId(), like.getLname());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Like like){
        Like like1=likeDao.findByBlogIdAndLname(like.getBlogId(), like.getLname());
        likeDao.delete(like1);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void add(Like like){
        likeDao.save(like);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean getWhetherLike(String username,Long blogId){
        return likeDao.existsByBlogIdAndLname(blogId, username);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
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

    public void deleteList(long blogId){
        likeDao.deleteAllByBlogId(blogId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Like> findLikeList(String username){
        List<Like> likeList=likeDao.findAllByUsername(username);
        List<Long> blogIdList=likeList.stream().map(Like::getBlogId).collect(Collectors.toList());
        List<String> lnameList=likeList.stream().map(Like::getLname).collect(Collectors.toList());
        if(blogIdList.size()>0){
            for (int i=0;i<blogIdList.size();i++){
                long blogId=blogIdList.get(i);
                String lname=lnameList.get(i);
                String likeBlogTitle=blogDao.findById(blogId).getTitle();
                String likeBlogPicture=blogDao.findById(blogId).getFirstPicture();
                String likeUserPicture=userDao.findByUsername(lname).getPictureUrl();
                Like like=likeList.get(i);
                like.setBlogTitle(likeBlogTitle);
                like.setBlogPicture(likeBlogPicture);
                like.setUserPicture(likeUserPicture);
                likeList.set(i,like);
            }
        }
        return likeList;
    }
}
