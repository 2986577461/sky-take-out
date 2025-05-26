package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sky.entity.Orders.CANCELLED;
import static com.sky.entity.Orders.DELIVERY_IN_PROGRESS;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;

    private UserMapper userMapper;

    private OrderDetailMapper orderDetailMapper;

    private AddressBookMapper addressBookMapper;

    private ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null)
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);

        Long userId = BaseContext.getCurrentId();

        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(ShoppingCart.
                builder().
                userId(userId).
                build());

        if (shoppingCarts == null || shoppingCarts.isEmpty())
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);


        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setAddress(addressBook.getDetail());
        orders.setOrderTime(LocalDateTime.now());
        orders.setNumber(UUID.randomUUID().toString());
        orders.setPhone(addressBook.getPhone());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);
        orderMapper.insert(orders);

        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insert(orderDetails);

        shoppingCartMapper.delete(ShoppingCart.
                builder().
                userId(userId).
                build());

        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .orderNumber(orders.getNumber())
                .build();
    }

    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        // 替代微信支付成功后的数据库订单状态更新，直接在这里更新了
        // 根据订单号查询当前用户的该订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(ordersPaymentDTO.getOrderNumber(), userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        orders.setCheckoutTime(LocalDateTime.now());
//        Orders orders = Orders.builder()
//                .id(ordersDB.getId())
//                .status(Orders.TO_BE_CONFIRMED) // 订单状态，待接单
//                .payStatus(Orders.PAID) // 支付状态，已支付
//                .checkoutTime(LocalDateTime.now()) // 更新支付时间
//                .build();

        orderMapper.update(orders);

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setNumber(outTradeNo);
        Orders ordersDB = orderMapper.pageQuery(ordersPageQueryDTO).get(0);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public List<OrderVO> historyOrders(Integer page, Integer pageSize, Integer status) {

        Long id = BaseContext.getCurrentId();
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(id);
        ordersPageQueryDTO.setStatus(status);
        List<Orders> list = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> orderVOS = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (Orders orders : list) {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(orders.getId()));
                orderVOS.add(orderVO);
            }

        return orderVOS;

    }

    @Override
    public OrderVO orderDetail(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(id));

        return orderVO;
    }

    @Override
    public void cancel(Long id) {
        Orders order = orderMapper.getById(id);

        if (order == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        if (!BaseContext.getCurrentId().equals(order.getUserId()))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        if (order.getStatus() > DELIVERY_IN_PROGRESS) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders order1 = new Orders();

        if (order.getPayStatus().equals(Orders.PAID)) {
            order1.setPayStatus(Orders.REFUND);
        }
        order1.setId(id);
        order1.setCancelReason("用户取消");
        order1.setCancelTime(LocalDateTime.now());
        order1.setStatus(CANCELLED);
        orderMapper.update(order1);
    }

    @Override
    public void repetition(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        List<OrderDetail> dishes = orderDetailMapper.getByOrderId(id);

        List<ShoppingCart> list = dishes.stream().map(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart, "id");
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartMapper.insertBatch(list);
    }

    @Override
    public PageResult canditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {

        List<Orders> orders = orderMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO> list = new ArrayList<>();

        orders.forEach(orders1 -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders1, orderVO);

            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders1.getId());
            orderVO.setOrderDishes(getOrderDishesStr(orderDetailList));

            list.add(orderVO);
        });

        return new PageResult(list.size(), list);
    }

    @Override
    public OrderStatisticsVO getStatistics() {

        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(DELIVERY_IN_PROGRESS);

        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);

        return orderStatisticsVO;
    }

    @Override
    public OrderVO details(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);

        orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(id));

        return orderVO;
    }

    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(ordersConfirmDTO.getStatus())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        Orders order = orderMapper.getById(ordersRejectionDTO.getId());
        if (order == null || order.getStatus() != Orders.TO_BE_CONFIRMED
                || order.getPayStatus() != Orders.PAID)
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        Orders freshOrder = new Orders();
        freshOrder.setId(order.getId());
        freshOrder.setStatus(Orders.CANCELLED);
        freshOrder.setPayStatus(Orders.REFUND);
        freshOrder.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        freshOrder.setCancelTime(LocalDateTime.now());

        orderMapper.update(freshOrder);

    }

    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        orderMapper.update(
                Orders.builder().
                        id(ordersCancelDTO.getId()).
                        status(Orders.CANCELLED).
                        cancelReason(ordersCancelDTO.getCancelReason()).
                        cancelTime(LocalDateTime.now()).
                        build());
    }

    private String getOrderDishesStr(List<OrderDetail> orderDetailList) {

        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }

}
