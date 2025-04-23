package com.sky.service.impl;


import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SetmealServiceImpl implements SetmealService {

    private final SetmealMapper setmealMapper;

    private final SetmealDishMapper setmealDishMapper;


    public SetmealServiceImpl(SetmealMapper setmealMapper, SetmealDishMapper setmealDishMapper) {
        this.setmealMapper = setmealMapper;
        this.setmealDishMapper = setmealDishMapper;
    }

    @Override
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.inset(setmeal);


        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish ->
                setmealDish.setSetmealId(setmeal.getId()));

        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public List<SetmealVO> query(SetmealPageQueryDTO setmealPageQueryDTO) {

        setmealPageQueryDTO.setPage(setmealPageQueryDTO.getPage() - 1);

        return setmealMapper.selectPage(setmealPageQueryDTO);
    }

    @Override
    public void delete(List<Long> ids) {
        ids.forEach(aLong ->
        {
            if (Objects.equals(setmealMapper.selectById(aLong).getStatus(), StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        });
        setmealDishMapper.deleteBatchBySetmealId(ids);
        setmealMapper.deleteBatch(ids);
    }

    @Override
    public SetmealVO getSetmealById(Long id) {
        SetmealVO setmealVO = setmealMapper.selectById(id);
        setmealVO.setSetmealDishes(setmealDishMapper.selectBySetmealId(id));
        return setmealVO;
    }

    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);


        ArrayList<Long> list = new ArrayList<>();
        list.add(setmeal.getId());
        setmealDishMapper.deleteBatchBySetmealId(list);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public void startOrStop(Long id, Integer status) {
        setmealMapper.update(Setmeal.builder().id(id).status(status).build());
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
