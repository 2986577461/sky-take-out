package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("page")
    public Result<PageResult> getPage(DishPageQueryDTO queryDTO) {
        log.info("菜品分页查询：{}", queryDTO);
        return Result.success(dishService.getPage(queryDTO));
    }

    @GetMapping("{id}")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        log.info("菜品回显:{}", id);
        return Result.success(dishService.getDishWithFlavorById(id));
    }

    @GetMapping("list")
    public Result<List<Dish>> getList(Long categoryId){
        log.info("菜品分类查询:{}",categoryId);
        return Result.success(dishService.getList(categoryId));
    }

    @DeleteMapping
    public Result<String> removes(@RequestParam List<Long> ids) {
        log.info("删除菜品：{}", ids);
        dishService.removes(ids);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);
        dishService.updateDishWithFlavor(dishDTO);
        return Result.success();
    }

    @PostMapping("status/{status}")
    public Result<String> switchStatus(@PathVariable Integer status, Long id) {
        log.info("{}账号{}", status==1 ? "启售" : "停售", id);
        dishService.switchStatus(id,status);
        return Result.success();
    }
}
