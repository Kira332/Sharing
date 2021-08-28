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
@Table(name = "focus")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Focus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull(message = "粉丝名不能为空")
    private String fansName;

    @Column
    @NotNull(message = "被关注者用户名不能为空")
    private String idolName;
}
