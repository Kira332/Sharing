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
@Table(name = "collection")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Collection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull(message = "收藏博客Id不能为空")
    private long blogId;

    @Column
    @NotNull(message = "收藏者用户名不能为空")
    private String collector;

    @Column
    @NotNull(message = "被收藏博客用户名不能为空")
    private String username;

}