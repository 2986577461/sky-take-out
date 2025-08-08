package com.xiaoyan.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@ToString
public class UserVO implements Serializable {

    private Long id;

    private String userName;

    @Schema(description="昵称")
    @NotBlank
    private String nickName;

    private String phonenumber;

    private String sex;

    private String email;
}
