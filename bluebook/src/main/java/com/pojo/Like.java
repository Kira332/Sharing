package com.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "likes")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Like implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull(message = "点赞博客id不能为空")
    private long blogId;

    @Column
    @NotNull(message = "点赞者不能为空")
    private String lname;

    @Column
    private String username;

    @Column
    private int isRead;
}
