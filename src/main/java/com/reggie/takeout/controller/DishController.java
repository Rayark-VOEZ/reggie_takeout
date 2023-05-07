package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.common.R;
import com.reggie.takeout.dto.DishDto;
import com.reggie.takeout.entity.Dish;
import com.reggie.takeout.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PutMapping
    public R<Object> updateDish(@RequestBody Dish dish) {

        return R.success(dishService.updateById(dish));
    }

    @GetMapping("/page")
    public R<Object> queryByPage(int page, int pageSize, String name) {

        Page<Dish> pageInfo = new Page<>(page, pageSize);

        // todo: 菜品分类不显示，可能需要多表联查
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getId);

        dishService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @GetMapping("/list")
    public R<Object> queryByList() {
        return null;
    }

    @DeleteMapping
    public R<Object> deleteDish() {
        return null;
    }

    @PostMapping("/status/{status}")
    public R<Object> batch() {
        return null;
    }

    @PostMapping
    public R<Object> save(@RequestBody DishDto dishDto) {

        dishService.saveWithFlavor(dishDto);

        return R.success("success");
    }

    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable Long id) {

        DishDto dishDto = dishService.getByIdWithFlavour(id);

        if (dishDto != null) {
            return R.success(dishDto);
        }
        else {
            return R.error("Error!");
        }
    }
}
