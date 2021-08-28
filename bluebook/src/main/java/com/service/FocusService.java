package com.service;

import com.Dao.FocusDao;
import com.Dao.UserDao;
import com.pojo.Blog;
import com.pojo.Focus;
import com.pojo.Like;
import com.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FocusService {

    @Autowired
    UserDao userDao;

    @Autowired
    FocusDao focusDao;

    public void getUserFocus(String username,String fans){
        Focus focus=new Focus();
        focus.setFansName(fans);
        focus.setIdolName(username);
        focusDao.save(focus);
    }

    public void deleteUserFocus(String username,String fans){
        Focus focus=new Focus();
        focus=focusDao.findByFansNameAndIdolName(fans, username);
        focusDao.delete(focus);
    }
    public List<User> getFocusListByUsername(String username){
        List<Focus> focusList=focusDao.findAllByFansName(username);
        List<String> idolNameList=focusList.stream().map(Focus::getIdolName).collect(Collectors.toList());
        List<User> idolList=new ArrayList<>();
        for (int i=0;i<idolNameList.size();i++){
            String idolName=idolNameList.get(i);
            User user=userDao.findByUsername(idolName);
            idolList.add(user);
        }
        return idolList;
    }

    public List<User> getFansListByUsername(String username){
        List<Focus> focusList=focusDao.findAllByIdolName(username);
        List<String> fansNameList=focusList.stream().map(Focus::getFansName).collect(Collectors.toList());
        List<User> fansList=new ArrayList<>();
        for (int i=0;i<fansNameList.size();i++){
            String fansName=fansNameList.get(i);
            User user=userDao.findByUsername(fansName);
            fansList.add(user);
        }
        return fansList;
    }
}
