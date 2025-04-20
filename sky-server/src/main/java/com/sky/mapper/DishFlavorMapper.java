package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertBatsh(@Param("flavors") List<DishFlavor> flavors);

    void deleteBatchByDishId(@Param("dishIds")List<Long> dishIds);

    List<DishFlavor> getFlavorByDishId(Long DishId);
}
