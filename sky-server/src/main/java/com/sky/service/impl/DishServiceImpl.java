package com.sky.service.impl;


import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setMealDishMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //插入一条菜品
        dishMapper.insert(dish);
        log.info("主键回显：{}", dish);
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
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
        if (setMealDishMapper.isAssociated(ids))
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);

        dishMapper.deleteBatch(ids);
        dishFlavorMapper.deleteBatchByDishId(ids);
    }

    @Override
    public DishVO getDishWithFlavorById(Long id) {
        Dish dish = dishMapper.selectById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavorMapper.getFlavorByDishId(id));
        return dishVO;
    }

    @Override
    @Transactional
    public void updateDishWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.update(dish);

        List<Long> id = new ArrayList<>();
        id.add(dish.getId());
        dishFlavorMapper.deleteBatchByDishId(id);

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.insertBatsh(flavors);
        }
    }

    @Override
    public void switchStatus(Long id, Integer status) {
        dishMapper.update(Dish.builder().id(id).status(status).build());
    }

    @Override
    public List<Dish> getList(Long categoryId) {
        return dishMapper.select(
                Dish.builder().
                        categoryId(categoryId).
                        status(StatusConstant.ENABLE).
                        build());
    }


    /**
     * 条件查询菜品和口味
     *
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.select(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getFlavorByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
