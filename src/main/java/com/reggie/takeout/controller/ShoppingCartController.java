package com.reggie.takeout.controller;

import com.reggie.takeout.common.R;
import com.reggie.takeout.entity.ShoppingCart;
import com.reggie.takeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @PutMapping
    public R<Object> updateShoppingCart() {
        return null;
    }

    @PostMapping("/sub")
    public R<Object> subShoppingCart() {
        return null;
    }

    @GetMapping("/page")
    public R<Object> queryByPage() {
        return null;
    }

    @DeleteMapping
    public R<Object> deleteShoppingCart() {
        return null;
    }

    @PostMapping
    public R<Object> saveShoppingCart() {
        return null;
    }

    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable Long id) {
        return null;
    }

    /**
     * 添加商品到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<Object> addShoppingCart(@RequestBody ShoppingCart shoppingCart) {

        /**
         * org.springframework.dao.DataIntegrityViolationException:
         * ### Error updating database.  Cause: java.sql.SQLException: Field 'user_id' doesn't have a default value
         * ### The error may exist in com/reggie/takeout/mapper/ShoppingCartMapper.java (best guess)
         * ### The error may involve com.reggie.takeout.mapper.ShoppingCartMapper.insert-Inline
         * ### The error occurred while setting parameters
         * ### SQL: INSERT INTO shopping_cart  ( id, name,   setmeal_id,   amount, image )  VALUES  ( ?, ?,   ?,   ?, ? )
         * ### Cause: java.sql.SQLException: Field 'user_id' doesn't have a default value
         * ; Field 'user_id' doesn't have a default value; nested exception is java.sql.SQLException: Field 'user_id' doesn't have a default value
         */

        if (shoppingCartService.save(shoppingCart)) {
            return R.success("成功！");
        }
        else {
            return R.error("失败！");
        }
    }

    @DeleteMapping("/clean")
    public R<Object> clearShoppingCart() {
        return null;
    }

    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping("/list")
    public R<Object> queryByList() {

        log.info("----- 查询购物车列表 -----");

        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        System.out.println(shoppingCartList);

        return R.success(shoppingCartList);
    }
}
