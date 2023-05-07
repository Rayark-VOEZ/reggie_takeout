package com.reggie.takeout.controller;

import com.reggie.takeout.common.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @PutMapping
    public R<Object> updateOrderDetail() {
        return null;
    }

    @GetMapping("/page")
    public R<Object> queryByPage() {
        return null;
    }

    @GetMapping("/list")
    public R<Object> queryByList() {
        return null;
    }

    @DeleteMapping
    public R<Object> deleteOrderDetail() {
        return null;
    }

    @PostMapping
    public R<Object> addOrderDetail() {
        return null;
    }

    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable Long id) {
        return null;
    }
}
