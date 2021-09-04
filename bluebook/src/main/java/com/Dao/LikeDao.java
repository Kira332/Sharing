package com.Dao;

import com.pojo.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeDao extends JpaRepository<Like,Long> {
    List<Like> findAllByUsername(String username);
    List<Like> findAllByUsernameAndIsRead(String username,int isRead);
    boolean existsByBlogIdAndLname(long blogId,String lname);
    Like findByBlogIdAndLname(long blogId,String lname);
    void deleteAllByBlogId(long blogId);
}
