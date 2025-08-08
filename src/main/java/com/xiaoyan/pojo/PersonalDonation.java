package com.xiaoyan.pojo;


import java.io.Serializable;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @TableName personal_donation
 */
@Data
@ToString
public class PersonalDonation implements Serializable {

    @NotNull(message = "[]不能为空")
    private Long id;
    /**
     *
     */
    @NotBlank(message = "[]不能为空")
    @Size(max = 20, message = "编码长度不能超过20")
    @Length(max = 20, message = "编码长度不能超过20")
    private String donator;
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    private Long donatorId;
    /**
     *
     */
    @Schema(description = "项目Id")
    private Long projectId;

    @NotBlank(message = "[]不能为空")
    @Size(max = 50, message = "编码长度不能超过50")
    @Length(max = 50, message = "编码长度不能超过50")
    private Integer type;

    @NotNull(message = "[]不能为空")
    private Integer amount;
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    private LocalDateTime createDateTime;

    /**
     *
     */


}
