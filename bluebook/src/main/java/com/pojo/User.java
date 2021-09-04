package com.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class User implements Serializable {
    @Id
    private String id;

    @Column
    @NotBlank(message = "⽤户名不能为空")
    private String username;

    @Column
    @NotBlank(message = "⽤户密码不能为空")
    @Length(min = 6,message = "密码⻓度⾄少6位")
    private String password;

    @Column
    private String salt;

    @Column
    @Length(min = 11,message = "手机号短于11位")
    private String phone;

    @Column
    @Range(min = 0,max = 2)
    private int sex;

    @Column
    private String signature;

    @Column
    @Email
    private String email;

    @Column
    @URL
    private String pictureUrl;

    @Column
    private int roleId;

    @Column
    private String lastTime;

    @Transient
    private Set<Role> roles;

    //判断是否互关(0是没有，1是有
    @Transient
    private int mutualInterest;

}
