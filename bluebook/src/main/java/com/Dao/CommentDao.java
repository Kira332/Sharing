package com.Dao;

import com.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment,Long> {
    List<Comment> findAllByUsername(String username);

    List<Comment> findAllByUsernameAndAndIsRead(String username,int isRead);

    List<Comment> findAllByBlogId(long blogId);

    @Query(value = "update comment set likes = likes - 1 where blog_id = #{blogId} and id = #{commentId}",nativeQuery = true)
    void updDesCommIsLikes(Long blogId, Long commentId);

    @Query(value = "update comment set likes = likes + 1 where blog_id = #{blogId} and id = #{commentId}",nativeQuery = true)
    void updInsCommIsLikes( Long blogId, Long commentId);

    @Query(value = "update comment set is_read = 0 where id = #{arg0}",nativeQuery = true)
    void updOneBlogNotComm(Long id);

    @Query(value = "update commentlikes set is_read = 0 where id = #{arg0}",nativeQuery = true)
    void updOneBlogNotLikes(Long id);
}
