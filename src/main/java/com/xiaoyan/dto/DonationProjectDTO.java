package com.xiaoyan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@ToString
@Data
public class DonationProjectDTO {

    @Schema(description = "修改时需要Id，创建不需要")
    private Long id;

    @NotBlank(message = "[]不能为空")
    @Size(max = 20, message = "编码长度不能超过20")
    @Length(max = 20, message = "编码长度不能超过20")
    @Schema(description = "项目名称")
    private String name;

    @NotBlank(message = "[]不能为空")
    @Size(max = 20, message = "编码长度不能超过20")
    @Length(max = 20, message = "编码长度不能超过20")
    @Schema(description = "用于分类前端展示")
    private String  type;

    @NotNull(message = "[]不能为空")
    @Schema(description = "书本金额")
    private Integer bookMoney;
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    @Schema(description = "教学工具金额")
    private Integer teachToolMoney;

    @NotNull(message = "[]不能为空")
    @Schema(description = "生活费用")
    private Integer liveMoney;

    @NotNull(message = "[]不能为空")
    private Integer medicalMoney;

    @NotBlank(message = "[]不能为空")
    @Size(max = 5000, message = "编码长度不能超过5000")
    @Length(max = 5000, message = "编码长度不能超过5000")
    private String projectContent;

    @NotNull(message = "[]不能为空")
    private LocalDateTime deadline;

    @Schema(description = "0项目已筹备完成  1未筹备完成")
    private Integer status;
}
