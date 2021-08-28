package com.service;

import com.Dao.BlogDao;
import com.Dao.UserDao;
import com.pojo.Blog;
import com.pojo.User;
import com.util.Constant;
import com.util.RedisOperator;
import com.util.StringAndArray;
import com.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;

@Service
public class BlogService {
    @Autowired
    BlogDao blogDao;
    @Autowired
    private RedisService redisService;

//    @Autowired
//    private EsService esService;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AsyncService asyncService;

    public List<Blog> findAllBlog(int page,int pageSize){
        Pageable pageable= PageRequest.of(page, pageSize);
        Page<Blog> blogPage=blogDao.findBlog(pageable);
        List<Blog> blogList=blogPage.getContent();
        return blogList;
    }

    public List<Blog> findAllBlogByType(int page,int pageSize,String type){
        Pageable pageable= PageRequest.of(page, pageSize);
        Page<Blog> blogPage=blogDao.findBlogsByType(pageable,type);
        List<Blog> blogList=blogPage.getContent();
        return blogList;
    }

    public int findAllBlogsPage(int pageSize){
        List<Blog> blogList=blogDao.findAll();
        int pageNum=blogList.size()/pageSize;
        int sign=blogList.size()%pageSize;
        if (sign==0){
            return pageNum;
        }else return pageNum+1;
    }

    public List<Blog> findAllByUsername(String username){
        List<Blog> blogList=blogDao.findAllByUsername(username);
        return blogList;
    }

    public List<Blog> findAllDraftByUsername(String username){
        List<Blog> blogList=blogDao.findAllByUsernameAndDraft(username,1);
        return blogList;
    }

    public Blog save(Blog blog){
        String userPicture=userDao.findByUsername(blog.getUsername()).getPictureUrl();
        if (!userPicture.equals(null)){
            blog.setUserPicture(userPicture);
        }
        blogDao.save(blog);
        return blog;
    }
    public Blog findByBolgId(long Id){
        return blogDao.findById(Id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Blog EchoBlogByBlogId(long id) {
        Blog blog = findBlogById(id);
//        String[] labels = StringAndArray.stringToArray(blogMessage.getLabelValues());
//        blog.setTagValue(labels);
        return blog;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Blog findBlogById(long id) {
        Blog blogMessage = null;
        if (redisOperator.hasHkey(Constant.BLOG_DETAIL, String.valueOf(id))) {
            if(redisOperator.hget(Constant.BLOG_DETAIL, String.valueOf(id)) == null){
                return blogMessage;
            }
            blogMessage = (Blog) redisOperator.hget(Constant.BLOG_DETAIL, String.valueOf(id));
            long looks = 0L;
            Integer likes = 0;

            if (redisOperator.hasKey(Constant.BLOG_DETAIL + id)) {
                looks = redisOperator.incr(Constant.BLOG_DETAIL + id, 1L);
                asyncService.updBlogLook(id, looks);
            } else {
                Blog findLikes = blogDao.findById(id);
                looks = findLikes.getView() + 1;
                redisOperator.set(Constant.BLOG_DETAIL + id, looks);
            }

            if (redisOperator.hasKey(Constant.BLOG_LIKES + id)) {
                likes = (int)redisOperator.get(Constant.BLOG_LIKES + id);
            } else {
                Blog findLikes = blogDao.findById(id);
                redisOperator.set(Constant.BLOG_LIKES + id, findLikes.getLikes());
            }
            blogMessage.setLikes(likes);
            blogMessage.setView(looks);

        } else {
            blogMessage = blogDao.findById(id);
            redisOperator.hset(Constant.BLOG_DETAIL, String.valueOf(id), blogMessage);
        }
        return blogMessage;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean updBlogByBlogId(Blog blogMessageVO) {
//        EsBlogMessage esBlogMessage = null;
        TimeUtil timeUtil = new TimeUtil();
        blogMessageVO.setCreatTime(timeUtil.getFormatDateForThree());
//        blogMessageVO.setTagValue(StringAndArray.stringToArray(blogMessageVO.getLabelValues()));
//        blogMessageVO.setArticleUrl("/article/" + blogMessageVO.getId());
//        int i = blogDao.updateById(blogMessageVO);
//        esBlogMessage = esService.findById(blogMessageVO.getId());
//        esService.removeEsBlog(blogMessageVO.getId());
//        esBlogMessage.update(blogMessageVO);
//        esService.saveBlog(esBlogMessage);
        redisOperator.lremove(Constant.PAGE_BLOG, 0, redisOperator.hget(Constant.BLOG_DETAIL, String.valueOf(blogMessageVO.getId())));
        redisOperator.lpush(Constant.PAGE_BLOG, blogMessageVO);
        redisOperator.hdel(Constant.BLOG_DETAIL, String.valueOf(blogMessageVO.getId()));
        redisOperator.hset(Constant.BLOG_DETAIL, String.valueOf(blogMessageVO.getId()), blogMessageVO);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void publishBlog(Blog blogMessage) {
        long id = 0L;
        Blog blog = null;
//        EsBlogMessage esBlogMessage = null;
        if (blogMessage.getId() == 0) {
            id = new TimeUtil().getLongTime();
            blogMessage.setId(id);
            blogMessage.setLikes(0);
            blogMessage.setView(0L);
            TimeUtil timeUtil = new TimeUtil();
            blogMessage.setCreatTime(timeUtil.getFormatDateForThree());
//            blogMessage.setTagValue(StringAndArray.stringToArray(blogMessage.getLabelValues()));
//            blogMessage.setArticleUrl("/article/" + blogMessage.getId());
            blogDao.save(blogMessage);
            blog = blogDao.findById(id);
//            esBlogMessage = new EsBlogMessage(blog);
        }
//        esService.saveBlog(esBlogMessage);
        redisService.SaveEditBlog(blog);
    }

    public void minusLike(long blogId){
        Blog blog=blogDao.findById(blogId);
        blog.setLikes(blog.getLikes()-1);
        blogDao.save(blog);
    }
    public void addLike(long blogId){
        Blog blog=blogDao.findById(blogId);
        blog.setLikes(blog.getLikes()+1);
        blogDao.save(blog);
    }

    public List<Blog> queryList(String query, Integer pagenum, Integer pageSize) {
        Specification<Blog> specification = new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path title = root.get("title");
                Predicate predicate = criteriaBuilder.like(title, "%" + query + "%");
                return predicate;
            }
        };
        PageRequest pageRequest = PageRequest.of(pagenum, pageSize);
        Page<Blog> page = blogDao.findAll(specification, pageRequest);
        List<Blog> blogList=page.getContent();
        return blogList;
    }

    public int findAllBlogsSearchPage(String query,int pageSize){
        Specification<Blog> specification = new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path title = root.get("title");
                Predicate predicate = criteriaBuilder.like(title, "%" + query + "%");
                return predicate;
            }
        };
        List<Blog> blogList=blogDao.findAll(specification);
        int pageNum=blogList.size()/pageSize;
        int sign=blogList.size()%pageSize;
        if (pageNum==0){
            return 1;
        }
        if (sign==0){
            return pageNum;
        }else return pageNum+1;
    }

}
