package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.common.BaseContext;
import com.reggie.takeout.common.R;
import com.reggie.takeout.dto.OrdersDto;
import com.reggie.takeout.entity.OrderDetail;
import com.reggie.takeout.entity.Orders;
import com.reggie.takeout.entity.User;
import com.reggie.takeout.service.OrderDetailService;
import com.reggie.takeout.service.OrdersService;
import com.reggie.takeout.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PutMapping
    public R<Object> updateOrder() {
        return null;
    }

    @PostMapping("/again")
    public R<Object> orderAgain() {
        return null;
    }

    /**
     * 分页查询订单信息
     * @param page 当前分页
     * @param pageSize 分页大小
     * @param number 订单号
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @GetMapping("/page")
    public R<Object> queryByPage(int page, int pageSize, String number, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        log.info("========== 分页查询订单信息 ==========");

        Page<Orders> ordersPage = new Page<>(page, pageSize);

        // todo: 查出来的数据没有用户信息，可能是插入数据时的问题

        // SELECT * FROM orders WHERE (number LIKE ? AND order_time BETWEEN ? AND ?) LIMIT ?
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null, Orders::getNumber, number);
        queryWrapper.between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime);

        return R.success(ordersService.page(ordersPage, queryWrapper));
    }

    @DeleteMapping
    public R<Object> deleteOrder() {
        return null;
    }

    /**
     * 提交订单信息
     * @param orders 表单
     * @return
     */
    @PostMapping("/submit")
    public R<Object> submitOrder(@RequestBody Orders orders) {

        log.info("========== 提交订单信息 ==========");

        ordersService.saveOrdersWithDetail(orders);

        return R.success("Success!");
    }

    @PostMapping
    public R<Object> addOrder() {
        return null;
    }

    @GetMapping("/{id}")
    public R<Object> queryById() {
        return null;
    }

    /**
     *
     * @param page 当前分页
     * @param pageSize 分页大小
     * @return orderDto分页数据
     */
    @GetMapping("/userPage")
    public R<Object> queryByPageForUser(int page, int pageSize) {

        log.info("==========  ==========");

        Page<Orders> ordersPage = new Page<>(page, pageSize);

        // SELECT * FROM orders WHERE user_id = ? LIMIT ?
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage, queryWrapper); // 分页查询订单信息

        Page<OrdersDto> ordersDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records"); // 将orders分页数据复制到orderDto分页数据中（排除records变量）

        ordersDtoPage.setRecords(
                ordersPage.getRecords().stream().map(orders -> { // 遍历orders分页数据的records变量
                    OrdersDto ordersDto = new OrdersDto();
                    BeanUtils.copyProperties(orders, ordersDto); // 将orders中的信息复制到orderDto中
                    return ordersDto;
                }).collect(Collectors.toList()) // 将遍历完的数据收集到列表中
        );

        ordersDtoPage.getRecords().forEach(ordersDto -> { // 遍历orderDto分页数据的records变量
            LambdaQueryWrapper<OrderDetail> orderDetailQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailQueryWrapper.eq(OrderDetail::getOrderId, ordersDto.getId()); // 查询条件：order_id

            ordersDto.setOrderDetails(orderDetailService.list(orderDetailQueryWrapper)); // 根据订单查询出所有的菜品信息，并复制给orderDto的orderDetails变量
        });

        return R.success(ordersDtoPage);
    }

    @GetMapping("/list")
    public R<Object> queryByList() {
        return null;
    }
}
