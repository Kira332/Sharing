package com.Dao;

import com.pojo.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentLikeDao extends JpaRepository<CommentLikes,Long> {
    List<CommentLikes> findByBlogIdAndCommentIdAndLikeName(long blogId,long commentId,String likeName);

}
