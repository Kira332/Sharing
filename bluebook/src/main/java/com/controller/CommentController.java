package com.controller;

import com.pojo.Comment;
import com.pojo.CommentLikes;
import com.pojo.ReportComment;
import com.pojo.User;
import com.result.Result;
import com.result.ResultFactory;
import com.service.BlogService;
import com.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;
    //发布评论
    @PostMapping("/addComment")
    public Result InsComment(@Valid Comment comment){
        Comment comments = new Comment();
        comments.setContent(comment.getContent());
        comments.setBlogId(comment.getBlogId());
        comments.setValuerName(comment.getValuerName());
        comments.setUsername(blogService.findByBolgId(comment.getBlogId()).getUsername());
        List<Comment> result = commentService.insComment(comments);
        if(result != null){
            return ResultFactory.buildSuccessResult("",result);
        }
        return ResultFactory.buildFailResult("新增评论失败");
    }

    /**
     * 新增回复评论
     * @param reportComment
     * @return
     */
    @PostMapping("/InsRepComment")
    public Result InsRepComment(@Valid ReportComment reportComment){

        List<Comment> allComment = commentService.insRepComment(reportComment);
        if(allComment != null){
            return ResultFactory.buildSuccessResult("",allComment);
        }else{
            return ResultFactory.buildFailResult("失败");
        }

    }

    /**
     * 评论查询
     * @return
     */
    @GetMapping("/getCommentDetail")
    public Result getCommentDetail(@RequestParam("blogId") long blogId){
        List<Comment> allComment = commentService.getAllComment(blogId);
        if(allComment != null){
            return ResultFactory.buildSuccessResult("",allComment);
        }else{
            return ResultFactory.buildFailResult("失败");
        }
    }

    /**
     * 点赞更新
     * @param commentLikes
     * @param request
     * @return
     */
    @PostMapping("/updLikes")
    public Result updLikes(@Valid CommentLikes commentLikes,
                                   HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultFactory.buildFailResult("用户已过期");
        }
        commentLikes.setLikeName(user.getUsername());
        boolean commLikes = commentService.isCommLikes(commentLikes);
        if(commLikes){
            // 已点赞
            List<Comment> comments = commentService.updDecCommLikes(commentLikes.getBlogId(), commentLikes.getCommentId());
            if(comments != null){
                commentService.delCommLikes(commentLikes);
                return ResultFactory.buildSuccessResult("",comments);
            }else{
                return ResultFactory.buildFailResult("点赞失败");
            }
        }else{
            // 未点赞
            commentService.insCommLikes(commentLikes);
            List<Comment> comments = commentService.updInsCommLikes(commentLikes.getBlogId(), commentLikes.getCommentId());
            return ResultFactory.buildSuccessResult("",comments);
        }
    }

    /**
     * 我的评论
     * @param username
     * @return
     */
    @GetMapping("getUserReport")
    public Result getUserReport(@RequestParam("username") String username){
        List<ReportComment> messNotRead = commentService.getUserRepMessNotRead(username);
        return ResultFactory.buildSuccessResult("",messNotRead);
    }

    /**
     * 全部消息已读
     * @param username
     * @return
     */
    @GetMapping("clearComNotRead")
    public Result clearComNotRead(@RequestParam("username") String username){
        List<ReportComment> reportComment = commentService.updComIsRead(username);
        if(reportComment != null){
            return ResultFactory.buildSuccessResult("",reportComment);
        }
        return ResultFactory.buildFailResult("错误");
    }

    /**
     * 部分评论消息已读
     * @param id
     * @return
     */
    @GetMapping("clearOneNotComm")
    public Result clearOneNotComm(@RequestParam("id") Long id){
        commentService.updOneNotComm(id);
        return ResultFactory.buildSuccessResult();
    }
}
