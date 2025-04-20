package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/setmeal")
@Slf4j
public class SetmealController {

    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        this.setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("page")
    public Result<PageResult> query(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询:{}", setmealPageQueryDTO);
        List<SetmealVO> list = setmealService.query(setmealPageQueryDTO);
        return Result.success(new PageResult(list.size(), list));
    }


}
