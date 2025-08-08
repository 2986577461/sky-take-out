package com.xiaoyan.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDTO {

    @Schema(description = "回显Id以便操作，注册时不需要")
    private Long id;

    @NotBlank
    private String userName;

    @NotBlank
    private String nickName;

    @Schema(description = "注册不需要")
    private String password;

    @Schema(description = "注册不需要")
    @NotBlank
    private String phonenumber;

    @NotBlank
    private String sex;

    @NotBlank
    private String email;

}
