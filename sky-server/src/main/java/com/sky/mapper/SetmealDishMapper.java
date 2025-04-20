package com.sky.mapper;


import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    boolean isAssociated(@Param("dishIds") List<Long>dishIds);

    void insertBatch(@Param("setmealDishes") List<SetmealDish> setmealDishes);
}
