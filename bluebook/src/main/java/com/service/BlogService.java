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
    private LikeService likeService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private AsyncService asyncService;

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Blog> findAllBlog(int page,int pageSize){
        Pageable pageable= PageRequest.of(page, pageSize);
        Page<Blog> blogPage=blogDao.findBlog(pageable);
        List<Blog> blogList=blogPage.getContent();
        return blogList;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
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

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Blog> findAllByUsername(String username){
        List<Blog> blogList=blogDao.findAllByUsername(username);
        return blogList;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Blog> findAllDraftByUsername(String username){
        List<Blog> blogList=blogDao.findAllByUsernameAndDraft(username,1);
        return blogList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Blog save(Blog blog){
        TimeUtil timeUtil = new TimeUtil();
        String userPicture=userDao.findByUsername(blog.getUsername()).getPictureUrl();
        if (!userPicture.equals(null)){
            blog.setUserPicture(userPicture);
        }
        blog.setCreatTime(timeUtil.getParseDateForSix());
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

    @Transactional(propagation = Propagation.SUPPORTS)
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

    public int deleteBlog(long blogId){
        if (blogDao.existsById(blogId)){
            blogDao.deleteById(blogId);
            likeService.deleteList(blogId);
            collectionService.deleteList(blogId);
            return 1;
        }else return 0;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
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
