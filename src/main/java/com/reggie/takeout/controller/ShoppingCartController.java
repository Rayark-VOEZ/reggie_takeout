package com.reggie.takeout.controller;

import com.reggie.takeout.common.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

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

    @PostMapping("/add")
    public R<Object> addShoppingCart() {
        return null;
    }

    @DeleteMapping("/clean")
    public R<Object> clearShoppingCart() {
        return null;
    }

    @GetMapping("/list")
    public R<Object> queryByList() {
        return null;
    }
}
