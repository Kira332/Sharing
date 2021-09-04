package com.service;

import com.Dao.FocusDao;
import com.Dao.UserDao;
import com.pojo.Blog;
import com.pojo.Focus;
import com.pojo.Like;
import com.pojo.User;
import net.sf.saxon.trans.SymbolicName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FocusService {

    @Autowired
    UserDao userDao;

    @Autowired
    FocusDao focusDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void getUserFocus(String username,String fans){
        Focus focus=new Focus();
        focus.setFansName(fans);
        focus.setIdolName(username);
        focusDao.save(focus);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserFocus(String username,String fans){
        Focus focus=focusDao.findByFansNameAndIdolName(fans, username);
        focusDao.delete(focus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
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

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> getFansListByUsername(String username){
        List<Focus> focusList=focusDao.findAllByIdolName(username);
        List<String> fansNameList=focusList.stream().map(Focus::getFansName).collect(Collectors.toList());
        List<User> fansList=new ArrayList<>();
        for (int i=0;i<fansNameList.size();i++){
            String fansName=fansNameList.get(i);
            User user=userDao.findByUsername(fansName);
            if(focusDao.existsByIdolNameAndFansName(fansName, username)){
                user.setMutualInterest(1);
            }else user.setMutualInterest(0);
            fansList.add(user);
        }
        return fansList;
    }

    public Boolean getIsFocus(String username,String fans){
        return focusDao.existsByIdolNameAndFansName(username,fans);
    }

}
