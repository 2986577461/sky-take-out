package com.xiaoyan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PersonalDonateDTO {

    @NotNull(message = "[]不能为空")

    @Schema(description = "项目Id")
    private Long projectId;

    @NotBlank(message = "[]不能为空")
    @Schema(description = "1生活费 2医疗费 3书本费 4教材费用")
    private Integer type;

    @NotNull
    @Schema(description = "捐赠金额")
    private Integer amount;

}
