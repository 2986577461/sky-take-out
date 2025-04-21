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

    @GetMapping("{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id) {
        log.info("套餐回显：{}", id);
        return Result.success(setmealService.getSetmealById(id));
    }

    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("删除套餐：{}", ids);
        setmealService.delete(ids);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }


}
