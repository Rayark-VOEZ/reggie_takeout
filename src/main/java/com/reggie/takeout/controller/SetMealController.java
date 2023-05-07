package com.reggie.takeout.controller;

import com.reggie.takeout.common.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @PutMapping
    public R<Object> update() {
        return null;
    }

    @GetMapping("/page")
    public R<Object> page() {
        return null;
    }

    @GetMapping("/list")
    public R<Object> list() {
        return null;
    }

    @DeleteMapping
    public R<Object> delete() {
        return null;
    }

    @PostMapping("/status/{status}")
    public R<Object> batch() {
        return null;
    }

    @PostMapping
    public R<Object> save() {
        return null;
    }

    @GetMapping("/{id}")
    public R<Object> queryById() {
        return null;
    }

    @GetMapping("/dish/{id}")
    public R<Object> queryAllDish() {
        return null;
    }
}
