package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    List<DishVO> selectPage(@Param("queryDTO") DishPageQueryDTO queryDTO);

    boolean hasOpenStatus(@Param("ids") List<Long> ids);

    void deleteBatch(@Param("ids") List<Long> ids);

    Dish selectById(Long id);

    @AutoFill(OperationType.UPDATE)
    void updateById(@Param("dish") Dish dish);
}
