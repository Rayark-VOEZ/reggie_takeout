package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.takeout.common.R;
import com.reggie.takeout.entity.ShoppingCart;
import com.reggie.takeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping("/list")
    public R<Object> queryByList(HttpSession session) {

        log.info("========== 查询购物车列表 ==========");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, session.getAttribute("userId"));

        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        return R.success(shoppingCartList);
    }

    @GetMapping("/page")
    public R<Object> queryByPage() {
        return null;
    }

    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable Long id) {
        return null;
    }

    /**
     * 从购物车减少商品
     * @param session Http会话
     * @param shoppingCart 购物车表单
     * @return
     */
    @PostMapping("/sub")
    public R<Object> subShoppingCart(HttpSession session, @RequestBody ShoppingCart shoppingCart) {

        log.info("========== 从购物车减少商品 ==========");

        if (shoppingCart == null) {
            return R.success("Success!");
        }

        shoppingCart.setUserId((Long) session.getAttribute("userId")); // 设置用户Id

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId()); // 查询条件: user_id
        queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId())
                .or()
                .eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId()); // 查询条件: dish_id or setmeal_id

        ShoppingCart one = shoppingCartService.getOne(queryWrapper);  // 查询购物车信息

        if (one.getNumber() == 1) { // 如果购物车数量等于1
            shoppingCartService.removeById(one); // 删除购物车信息
        }
        else {
            one.setNumber(one.getNumber() - 1); // 数量-1
            shoppingCartService.updateById(one); // 根据Id更新购物车信息
        }

        return R.success("Success!");
    }

    @PostMapping
    public R<Object> saveShoppingCart() {
        return null;
    }

    /**
     * 添加商品到购物车
     * @param session Http会话
     * @param shoppingCart 购物车表单
     * @return R - 成功
     */
    @PostMapping("/add")
    public R<Object> addShoppingCart(HttpSession session, @RequestBody ShoppingCart shoppingCart) {

        log.info("========== 添加商品到购物车 ==========");

        shoppingCart.setUserId((Long) session.getAttribute("userId")); // 设置用户Id
        shoppingCart.setCreateTime(LocalDateTime.now()); // 设置创建时间

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId()); // 查询条件: user_id
        queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId())
                .or()
                .eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId()); // 查询条件: dish_id or setmeal_id

        ShoppingCart one = shoppingCartService.getOne(queryWrapper);  // 查询购物车信息

        if (one != null) { // 如果存在购物车信息
            one.setNumber(one.getNumber() + 1); // 数量+1
            shoppingCartService.updateById(one); // 更新购物车信息
        }
        else {
            shoppingCartService.save(shoppingCart); // 插入购物车信息
        }

        return R.success("Success!");
    }

    @PutMapping
    public R<Object> updateShoppingCart() {
        return null;
    }


    @DeleteMapping
    public R<Object> deleteShoppingCart() {
        return null;
    }

    /**
     * 清空购物车信息
     * @param session Http会话
     * @return
     */
    @DeleteMapping("/clean")
    public R<Object> clearShoppingCart(HttpSession session) {

        log.info("========== 清空购物车信息 ==========");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, session.getAttribute("userId"));

        if (shoppingCartService.remove(queryWrapper)) {
            return R.success("Success!");
        }
        else {
            return R.error("Error!");
        }
    }
}
