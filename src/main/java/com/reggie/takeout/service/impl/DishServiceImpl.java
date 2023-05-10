package com.reggie.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dto.DishDto;
import com.reggie.takeout.entity.Dish;
import com.reggie.takeout.entity.DishFlavor;
import com.reggie.takeout.mapper.DishMapper;
import com.reggie.takeout.service.DishFlavorService;
import com.reggie.takeout.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     *
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        this.save(dishDto);

        List<DishFlavor> flavorList = dishDto.getFlavors();

        flavorList = flavorList.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavorList);
    }

    /**
     *
     * @param dishDto
     */
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {

        this.updateById(dishDto);

        List<DishFlavor> flavorList = dishDto.getFlavors();

        flavorList = flavorList.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveOrUpdateBatch(flavorList);
    }

    /**
     *
     * @param id
     * @return DishDto
     */
    @Override
    public DishDto getByIdWithFlavour(Long id) {

        Dish dish = this.getById(id); // 根据菜品id获取菜品信息

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto); // 将dish中的属性复制到dishDto

        // 根据菜品id查询菜品风味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(list);

        return dishDto;
    }
}
