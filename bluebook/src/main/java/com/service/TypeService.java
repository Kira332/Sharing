package com.service;

import com.Dao.TypeDao;
import com.pojo.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    TypeDao typeDao;

    public List<Type> findAllTags(){
        return typeDao.findAll();
    }
}
