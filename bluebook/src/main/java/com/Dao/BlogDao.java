package com.Dao;

import com.pojo.Blog;
import com.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface BlogDao extends JpaRepository<Blog,Long> , JpaSpecificationExecutor<Blog> {
    @Query(value = "SELECT * FROM blog WHERE draft=0", nativeQuery = true)
    Page<Blog> findBlog(Pageable pageable);
    Blog findById(long id);
    @Query(value = "SELECT * FROM blog WHERE type = :typeName AND draft=0", nativeQuery = true)
    Page<Blog> findBlogsByType(Pageable pageable,@Param("typeName")String type);
    List<Blog> findAllByUsername(String username);
    List<Blog> findAllByUsernameAndDraft(String username,int draft);
}
