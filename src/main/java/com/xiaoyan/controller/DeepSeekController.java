package com.xiaoyan.controller;


import com.xiaoyan.context.BaseContext;
import com.xiaoyan.dto.MessageDTO;
import com.xiaoyan.result.Result;
import com.xiaoyan.service.DeepSeekService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ai-dialog")
@AllArgsConstructor
@Tag(name = "DeepSeek管理")
@Slf4j
public class DeepSeekController {

    private DeepSeekService deepSeekService;

    @PostMapping
    @Operation(summary = "发送消息")
    public Result<String> send(@RequestBody@Valid MessageDTO messageDTO) {
        log.info("AI问答:{}", messageDTO);
        String message = deepSeekService.send(BaseContext.getCurrentId(), messageDTO.getMessage());
        return Result.success(message);
    }
}
