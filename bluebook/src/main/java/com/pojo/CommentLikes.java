package com.pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "commentlikes")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class CommentLikes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "博客Id不能为空")
    private Long blogId;

    private Long commentId;

    @NotNull(message = "点赞者用户名不能为空")
    private String likeName;

    private String likeTime;

    private Integer isRead = 1;

    //博客标题
    @Transient
    private String title;
}
