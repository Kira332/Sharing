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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blog")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String title;

    @Lob
    private String content;

    @Column
    private String creatTime;

    @Column
    @URL
    private String firstPicture;

    @Column
    private int permission;

    @Column
    @NotNull(message = "用户名不能为空")
    private String username;

    @Column
    private String type;

    @Column
    @Min(0)
    private int likes;

    @Column
    @Min(0)
    private long view;

    @Column
    private int draft;

    @Column
    @URL
    private String userPicture;
}
