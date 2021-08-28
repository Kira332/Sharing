package com.Dao;

import com.pojo.CommentLikes;
import com.pojo.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepCommentDao extends JpaRepository<ReportComment,Long> {
    List<ReportComment> findAllByComName(String comName);
    List<ReportComment> findAllByComNameAndRisRead(String comName,int isRead);

}
