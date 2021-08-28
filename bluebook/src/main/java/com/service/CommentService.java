package com.service;

import com.Dao.CommentDao;
import com.Dao.CommentLikeDao;
import com.Dao.RepCommentDao;
import com.Dao.UserDao;
import com.pojo.Comment;
import com.pojo.CommentLikes;
import com.pojo.Like;
import com.pojo.ReportComment;
import com.util.Constant;
import com.util.RedisOperator;
import com.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private RepCommentDao repCommentDao;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private CommentLikeDao commentLikeDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void insCommLikes(CommentLikes commentLikes) {
        TimeUtil timeUtil = new TimeUtil();
        long id = timeUtil.getLongTime();
        commentLikes.setId(id);
        commentLikes.setLikeTime(timeUtil.getParseDateForSix());
        commentLikeDao.save(commentLikes);
    }

    public void updOneNotComm(Long id) {
        ReportComment reportComment = new ReportComment();
        reportComment.setId(id);
        reportComment.setRisRead(0);
        System.out.println("HI");
        repCommentDao.save(reportComment);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ReportComment> updComIsRead(String username) {
        List<ReportComment> userRepMessNotRead = repCommentDao.findAllByComName(username);
        if(userRepMessNotRead.size() == 0){
            return userRepMessNotRead;
        }
        List<ReportComment> reportComments = repCommentDao.findAllByComNameAndRisRead(username, 0);
        if (reportComments!=null && !reportComments.isEmpty()){
            reportComments.forEach(reportComment -> reportComment.setRisRead(1));
            repCommentDao.saveAll(reportComments);
        }
        userRepMessNotRead = getUserRepMessNotRead(username);
        return userRepMessNotRead;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comment> updDecCommLikes(Long blogId, Long commentId) {
        commentDao.updDesCommIsLikes(blogId, commentId);
        List<Comment> allComment = null;
        redisOperator.lpop(Constant.BLOG_REPORT + blogId);
        getAllComment(blogId);
        allComment = (List<Comment>) redisOperator.range(Constant.BLOG_REPORT + blogId, 0, -1);
        return allComment;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delCommLikes(CommentLikes commentLikes) {
        commentLikeDao.delete(commentLikes);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ReportComment> getUserRepMessNotRead(String username) {
        return repCommentDao.findAllByComNameAndRisRead(username,0);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comment> updInsCommLikes(Long blogId, Long commentId) {
        commentDao.updInsCommIsLikes(blogId, commentId);
        List<Comment> allComment = null;
        redisOperator.lpop(Constant.BLOG_REPORT + blogId);
        getAllComment(blogId);
        allComment = (List<Comment>) redisOperator.range(Constant.BLOG_REPORT + blogId, 0, -1);
        return allComment;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean isCommLikes(CommentLikes commentLikes) {
        long blogId=commentLikes.getBlogId();
        long commentId=commentLikes.getCommentId();
        String likeName=commentLikes.getLikeName();
        return commentLikeDao.findByBlogIdAndCommentIdAndLikeName(blogId,commentId,likeName) != null ? true : false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comment> insRepComment(ReportComment reportComment) {
        TimeUtil timeUtil = new TimeUtil();
        long id = timeUtil.getLongTime();
        reportComment.setId(id);
        reportComment.setRcreateTime(timeUtil.getParseDateForSix());
        reportComment.setRisRead(1);
        List<Comment> list = null;
        repCommentDao.save(reportComment);
        list = commentDao.findAllByBlogId(reportComment.getBlogId());
        redisOperator.lpop(Constant.BLOG_REPORT + reportComment.getBlogId());
        redisOperator.lpush(Constant.BLOG_REPORT + reportComment.getBlogId(), list);
        list = getAllComment(reportComment.getBlogId());
        redisOperator.incr(Constant.BLOG_REPORT_COUNT, 1);
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Comment> getAllComment(long blogId) {
        List<Comment> list = null;
        if (redisOperator.hasKey(Constant.BLOG_REPORT + blogId)) {
            list = (List<Comment>) redisOperator.range(Constant.BLOG_REPORT + blogId, 0, -1);
        } else {
            list = commentDao.findAllByBlogId(blogId);
            if (list.size() > 0) {
                redisOperator.lpush(Constant.BLOG_REPORT + blogId, list);
            } else {
                redisOperator.lpush(Constant.BLOG_REPORT + blogId, "");
            }
        }
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comment> insComment(Comment comment) {
        TimeUtil timeUtil = new TimeUtil();
        long id = timeUtil.getLongTime();
        comment.setId(id);
        comment.setCreatTime(timeUtil.getParseDateForSix());
        comment.setLikes(0);
        comment.setUserPicture(userDao.findByUsername(comment.getValuerName()).getPictureUrl());
        List<Comment> byBlogId = null;
        List<Comment> list = null;
        commentDao.save(comment);
        long blogId = comment.getBlogId();
        byBlogId = commentDao.findAllByBlogId(blogId);
        redisOperator.lpop(Constant.BLOG_REPORT + blogId);
        redisOperator.lpush(Constant.BLOG_REPORT + blogId, byBlogId);
        list = getAllComment(blogId);
        redisOperator.incr(Constant.BLOG_REPORT_COUNT, 1);
        return list;
    }
}
