package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertBatsh( List<DishFlavor> flavors);

    void deleteBatchByDishId(List<Long> dishIds);

    List<DishFlavor> getFlavorByDishId(Long DishId);
}
