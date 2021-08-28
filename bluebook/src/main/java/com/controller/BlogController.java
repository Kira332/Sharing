package com.controller;

import com.Dao.BlogDao;
import com.pojo.Blog;
import com.pojo.Collection;
import com.pojo.Like;
import com.pojo.User;
import com.result.Result;
import com.result.ResultFactory;
import com.service.BlogService;
import com.service.CollectionService;
import com.service.LikeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class BlogController {
    @Autowired
    BlogService blogService;

    @Autowired
    LikeService likeService;

    @Autowired
    CollectionService collectionService;

    //首页
    @GetMapping("/home")
    public Result myArticles(Integer pageSize,Integer pageNum) {
        List<Blog> allBlog = blogService.findAllBlog(pageNum,pageSize);
        return ResultFactory.buildSuccessResult("",allBlog);
    }

    //文章详情
    @GetMapping("getBlogDetail")
    public Result getBlogDetail(long blogId) {
        Blog byId = blogService.findByBolgId(blogId);
        if(byId != null){
            return ResultFactory.buildSuccessResult("",byId);
        }else{
            return ResultFactory.buildFailResult("已删除");
        }

    }

    //是否拥有写博客权限
    @GetMapping("/isNotPermission")
    public Result isNotPermission(){
        Subject subject = SecurityUtils.getSubject();
        if(!subject.hasRole("admin")){
            return ResultFactory.buildFailResult("无角色功能");
        }
        return ResultFactory.buildSuccessResult();
    }

    //标签页分类
    @GetMapping("/getTagsDetail")
    public Result getTagsDetail(Integer pageSize,Integer pageNum,String tags){
        List<Blog> allBlog = blogService.findAllBlogByType(pageNum,pageSize,tags);
        return ResultFactory.buildSuccessResult("",allBlog);
    }

    //获得个人博客
    @GetMapping("/getMyBlogs")
    public Result getMyBlogs(String username){
        List<Blog> allBlog = blogService.findAllByUsername(username);
        return ResultFactory.buildSuccessResult("",allBlog);
    }

    //点赞
    @GetMapping("/getBlogLike")
    public Result getBlogLike(long blogId,String username){
        Like like=new Like();
        like.setLname(username);
        like.setBlogId(blogId);
        like.setUsername(blogService.findByBolgId(blogId).getUsername());
        boolean wheatherLike = likeService.getWhetherLike(username, blogId);
        if (wheatherLike){
            blogService.minusLike(blogId);
            likeService.delete(like);
            return ResultFactory.buildSuccessResult();
        }else {
            blogService.addLike(blogId);
            likeService.add(like);
            return ResultFactory.buildSuccessResult();
        }
    }

    //收藏
    @GetMapping("/getBlogCollection")
    public Result getBlogCollection(long blogId,String username){
        Collection collection=new Collection();
        collection.setCollector(username);
        collection.setBlogId(blogId);
        collection.setUsername(blogService.findByBolgId(blogId).getUsername());
        boolean isCollection = collectionService.isCollection(collection);
        if (isCollection){
            collectionService.delete(collection);
            return ResultFactory.buildSuccessResult();
        }else {
            collectionService.add(collection);
            return ResultFactory.buildSuccessResult();
        }
    }

    //关键字查询
    @PostMapping("/queryBlog")
    public Result queryBlog(String query,Integer pageSize,Integer pageNum){
        List<Blog> blogPage=blogService.queryList(query, pageNum, pageSize);
        return ResultFactory.buildSuccessResult("",blogPage);
    }

    //返回搜索页总页数
    @GetMapping("/totalPage")
    public Result totalPage(Integer pageSize,String query){
        int blogPage=blogService.findAllBlogsSearchPage(query, pageSize);
        return ResultFactory.buildSuccessResult("",blogPage);
    }
}
