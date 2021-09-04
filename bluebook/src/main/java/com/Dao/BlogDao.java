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

import java.sql.Blob;
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

    @Query(value = "SELECT UNCOMPRESS(content) FROM blog WHERE id = :blogsid",nativeQuery = true)
    byte[] uncompressContent(@Param("blogsid")long blogsid);
    @Query(value = "UPDATE blog SET content = COMPRESS( :setcontent ) WHERE id= :blogid",nativeQuery = true)
    void compressContent (@Param("setcontent")String setcontent,@Param("blogid")long blogid);

    //compress的查询版本
    @Query(value = "SELECT id,title,UNCOMPRESS(content) AS content,creat_time,first_picture,permission,username,likes,view,draft,type,user_picture FROM blog WHERE type = :typeName AND draft=0", nativeQuery = true)
    Page<Blog> findBlogsByType1(Pageable pageable,@Param("typeName")String type);
    @Query(value = "SELECT id,title,UNCOMPRESS(content) AS content,creat_time,first_picture,permission,username,likes,view,draft,type,user_picture FROM blog WHERE username = :setusername AND draft=0", nativeQuery = true)
    List<Blog> findAllByUsername1(@Param("setusername")String setusername);
    //后续要用的时候会在接口里读byte[]

}
