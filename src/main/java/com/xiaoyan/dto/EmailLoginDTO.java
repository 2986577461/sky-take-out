package com.xiaoyan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EmailLoginDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String code;
}
