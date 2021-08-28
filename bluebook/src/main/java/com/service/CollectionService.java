package com.service;

import com.Dao.BlogDao;
import com.Dao.CollectionDao;
import com.pojo.Blog;
import com.pojo.Collection;
import com.pojo.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionService {
    @Autowired
    CollectionDao collectionDao;

    @Autowired
    BlogDao blogDao;
    public boolean getIsCollection(String cname,long blogId){
        boolean sign=collectionDao.existsByCollectorAndBlogId(cname, blogId);
        return sign;
    }

    public void delete(Collection collection){
        Collection collection1=collectionDao.findByBlogIdAndCollector(collection.getBlogId(), collection.getCollector());
        collectionDao.delete(collection1);
    }

    public void add(Collection collection){
        collectionDao.save(collection);
    }

    public boolean isCollection(Collection collection) {
        return collectionDao.existsByCollectorAndBlogId(collection.getCollector(),collection.getBlogId());
    }

    public List<Blog> findCollections(String username){
        List<Collection> collectionList=collectionDao.findAllByCollector(username);
        List<Long> blogIdList=collectionList.stream().map(Collection::getBlogId).collect(Collectors.toList());
        List<Blog> blogList=new ArrayList<>();
        for (int i=0;i<blogIdList.size();i++){
            long blogId=blogIdList.get(i);
            Blog collector=blogDao.findById(blogId);
            blogList.add(collector);
        }
        return blogList;
    }

}
