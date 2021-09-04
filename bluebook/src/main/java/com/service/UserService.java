package com.service;

import com.Dao.*;
import com.pojo.*;
import com.util.ShiroMD5;
import org.elasticsearch.client.license.LicensesStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    LikeDao likeDao;
    @Autowired
    CollectionDao collectionDao;
    @Autowired
    CommentDao commentDao;

    @Transactional(propagation = Propagation.SUPPORTS)
    public User findUserMess(String username){
        return userDao.findByUsername(username);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User changeUserMess(User giveUser){
        User user=userDao.findByUsername(giveUser.getUsername());
        giveUser.setPassword(user.getPassword());
        giveUser.setId(user.getId());
        giveUser.setPhone(user.getPhone());
        giveUser.setLastTime(user.getLastTime());
        userDao.save(giveUser);
        return findUserMess(giveUser.getUsername());
    }

    public List<User> findCollection(String username){
        List<Collection> collectionList=collectionDao.findAllByCollector(username);
        List<String> collectorList=collectionList.stream().map(Collection::getCollector).collect(Collectors.toList());
        List<User> collectors=new ArrayList<>();
        for (int i=0;i<collectorList.size();i++){
            String collectorname=collectorList.get(i);
            User collector=userDao.findByUsername(collectorname);
            collectors.add(collector);
        }
        return collectors;
    }


    public void clearComment(String username) {
        List<Comment> commentList = commentDao.findAllByUsernameAndAndIsRead(username,0);
        if (commentList!=null && !commentList.isEmpty()){
            commentList.forEach(comment -> comment.setIsRead(1));
            commentDao.saveAll(commentList);
        }
    }

    public void clearLikes(String username) {
        List<Like> likeList = likeDao.findAllByUsernameAndIsRead(username, 0);
        if (likeList!=null && !likeList.isEmpty()){
            likeList.forEach(like -> like.setIsRead(1));
            likeDao.saveAll(likeList);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updUserPwd(String phone, String password) {
        Object o = ShiroMD5.MD5(phone, password);
        userDao.updUserPwd(phone, String.valueOf(o));
    }

    public User findUserByPhone(String phone){
        return userDao.findByPhone(phone);
    }


}
