package com.xiaoyan.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaoyan.result.Result;
import com.xiaoyan.service.SmartBusService;
import com.xiaoyan.vo.SmartBusVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("bus")
@Tag(name = "智慧巴士")
@AllArgsConstructor
public class SmartBusController {

    private SmartBusService smartBusService;

    @GetMapping("all")
    @Operation(summary = "获取所有地点的巴士数据")
    public Result<List<SmartBusVO>> getAll() throws JsonProcessingException {
        List<SmartBusVO> list = smartBusService.getAll();
        return Result.success(list);
    }

}
