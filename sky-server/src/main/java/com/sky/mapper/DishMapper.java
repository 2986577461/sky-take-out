package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    List<DishVO> selectPage( DishPageQueryDTO queryDTO);

    boolean hasOpenStatus( List<Long> ids);

    void deleteBatch( List<Long> ids);

    List<Dish> select( Dish dish);

    @AutoFill(OperationType.UPDATE)
    void update( Dish dish);

}
