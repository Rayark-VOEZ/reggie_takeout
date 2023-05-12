package com.reggie.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.common.BaseContext;
import com.reggie.takeout.entity.*;
import com.reggie.takeout.mapper.OrdersMapper;
import com.reggie.takeout.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;

    /**
     *
     * @param orders
     */
    @Transactional
    public void saveOrdersWithDetail(Orders orders) {

        // SELECT * FROM user WHERE id=?
        User user = userService.getById(BaseContext.getCurrentId()); // 查询用户信息
        // SELECT * FROM address_book WHERE id=? AND is_deleted=0
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId()); // 查询地址信息

        // SELECT * FROM shopping_cart WHERE user_id = ?
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId()); // 查询条件：userId

        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper); // 从购物车中查询出所有菜品信息
        List<OrderDetail> orderDetailList = new ArrayList<>();

        long orderId = IdWorker.getId(); // 利用工具类生成Id
        BigDecimal sum = BigDecimal.valueOf(0);

        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetailList.add(orderDetail);

            sum = sum.add(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber()))); // 计算订单金额
        }

        orders.setId(orderId); // 设置订单Id
        orders.setNumber(UUID.randomUUID().toString()); // 生成订单编号
        orders.setUserId(user.getId()); // 设置用户Id
        orders.setOrderTime(LocalDateTime.now()); // 设置下单时间
        orders.setCheckoutTime(LocalDateTime.now()); // 设置付款时间
        orders.setAmount(sum); // 设置订单金额
        orders.setPhone(addressBook.getPhone()); // 设置订单收货人电话
        orders.setAddress(addressBook.getDetail()); // 设置订单收货人地址
        orders.setUserName(user.getName()); // 设置订单下单人
        orders.setConsignee(addressBook.getConsignee()); // 设置订单收货人姓名

        this.save(orders); //

        // INSERT INTO orders ( id, number, user_id, address_book_id, order_time, checkout_time, pay_method, amount, remark, phone, address, consignee ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
        orderDetailService.saveBatch(orderDetailList); // 批量添加订单菜品信息
        shoppingCartService.removeBatchByIds(shoppingCartList); // 清空购物车
    }
}
