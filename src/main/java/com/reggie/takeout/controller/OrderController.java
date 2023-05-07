package com.reggie.takeout.controller;

import com.reggie.takeout.common.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @PutMapping
    public R<Object> updateOrder() {
        return null;
    }

    @PostMapping("/again")
    public R<Object> orderAgain() {
        return null;
    }

    @GetMapping("/page")
    public R<Object> queryByPage() {
        return null;
    }

    @DeleteMapping
    public R<Object> deleteOrder() {
        return null;
    }

    @PostMapping("/submit")
    public R<Object> submitOrder() {
        return null;
    }

    @PostMapping
    public R<Object> addOrder() {
        return null;
    }

    @GetMapping("/{id}")
    public R<Object> queryById() {
        return null;
    }

    @GetMapping("/userPage")
    public R<Object> queryByPageForUser() {
        return null;
    }

    @GetMapping("/list")
    public R<Object> queryByList() {
        return null;
    }
}
