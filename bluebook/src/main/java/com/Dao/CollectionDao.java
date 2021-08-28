package com.Dao;

import com.pojo.Collection;
import com.pojo.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionDao extends JpaRepository<Collection,Long> {
    List<Collection> findAllByCollector(String username);
    boolean existsByCollectorAndBlogId(String collector,long blogId);
    Collection findByBlogIdAndCollector(long blogId,String collector);
}
