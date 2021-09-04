package com.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    @NotNull(message = "评论者不能为空")
    private String valuerName;

    @Column
    @NotNull(message = "博客Id不能为空")
    private long blogId;

    @Column
    private String creatTime;

    @Column
    @Min(0)
    private int likes;

    @Column
    private int isRead;

    @Column
    private String content;

    @Column
    @URL
    private String userPicture;

    @Transient
    private String blogTitle;
}
