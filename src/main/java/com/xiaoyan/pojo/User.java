package com.xiaoyan.pojo;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User implements Serializable {

    private Long id;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String userName;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String nickName;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String password;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String phonenumber;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String sex;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String email;

}
