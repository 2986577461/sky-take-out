package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult getPage(DishPageQueryDTO queryDTO);


    void removes(List<Long> ids);

    DishVO getDishWithFlavorById(Long id);

    void updateDishWithFlavor(DishDTO dishDTO);

    void switchStatus(Long id, Integer status);

    List<Dish> getList(Long categoryId);
}
