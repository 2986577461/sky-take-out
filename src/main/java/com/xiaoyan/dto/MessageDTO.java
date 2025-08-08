package com.xiaoyan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageDTO {

    @NotBlank
    String message;
}
