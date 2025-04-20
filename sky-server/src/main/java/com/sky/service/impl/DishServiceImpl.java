package com.sky.service.impl;


import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //插入一条菜品
        dishMapper.insert(dish);
        log.info("主键回显：{}", dish);
        Long DishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(DishId));
            dishFlavorMapper.insertBatsh(flavors);
        }

    }

    @Override
    public PageResult getPage(DishPageQueryDTO queryDTO) {
        queryDTO.setPage(queryDTO.getPage() - 1);
        List<DishVO> dishVOS = dishMapper.selectPage(queryDTO);
        return new PageResult(dishVOS.size(), dishVOS);
    }

    @Override
    @Transactional
    public void removes(List<Long> ids) {
        if (dishMapper.hasOpenStatus(ids))
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        if(setMealDishMapper.isAssociated(ids))
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);

        dishMapper.deleteBatch(ids);
        dishFlavorMapper.deleteBatchByDishId(ids);

    }


}
