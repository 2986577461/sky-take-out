package com.sky.controller.user;


import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userOrderController")
@RequestMapping("user/order")
@Slf4j
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping("submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        OrderSubmitVO orderSubmitVO=orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        return Result.success(orderPaymentVO);
    }

    @GetMapping("historyOrders")
    public Result<PageResult> historyOrders(Integer page,Integer pageSize,Integer status){
        log.info("历史订单：{},{},{}",page,pageSize,status);
        List<OrderVO> orderVO = orderService.historyOrders(page, pageSize,status);
        return Result.success(new PageResult(orderVO.size(),orderVO));
    }

    @GetMapping("orderDetail/{id}")
    public Result<OrderVO> orderDetail(@PathVariable Long id){
        log.info("订单详情：{}",id);
        return Result.success(orderService.orderDetail(id));
    }

    @PutMapping("cancel/{id}")
    public Result<String> cancel(@PathVariable Long id){
        log.info("取消订单：{}",id);
        orderService.cancel(id);
        return Result.success();
    }

    @PostMapping("repetition/{id}")
    public Result<String>repetition(@PathVariable Long id){
        log.info("再来一单：{}",id);
        orderService.repetition(id);
        return Result.success();
    }


}
