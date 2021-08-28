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
@Table(name = "reportcomment")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class ReportComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long commentId;

    @NotNull(message = "回复评论内容不能为空")
    private String repMess;

    private String reportedId;

    private String rcreateTime;

    private Integer risRead = 0;

    private String repName;

    private String comName;

    @Transient
    private Long blogId;

    //博客标题
    @Transient
    private String title;
}
