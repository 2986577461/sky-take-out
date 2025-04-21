package com.sky.mapper;


import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    boolean isAssociated(List<Long> dishIds);

    void insertBatch(List<SetmealDish> setmealDishes);

    List<SetmealDish> selectBySetmealId(Long setmealId);

    void deleteBatchBySetmealId(List<Long> setmealIds);
}
