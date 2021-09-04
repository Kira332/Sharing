package com.service;

import com.Dao.TypeDao;
import com.pojo.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    TypeDao typeDao;

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Type> findAllTags(){
        return typeDao.findAll();
    }
}
