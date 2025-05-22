package com.sky.mapper;


import com.sky.dto.OrdersDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    List<Orders> query(@Param("ordersDTO") OrdersDTO ordersDTO, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

    @Select("SELECT * FROM orders WHERE id=#{id}")
    Orders getById(Long id);
}
