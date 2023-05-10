package com.reggie.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dto.SetmealDto;
import com.reggie.takeout.entity.Setmeal;
import com.reggie.takeout.entity.SetmealDish;
import com.reggie.takeout.mapper.SetmealMapper;
import com.reggie.takeout.service.DishService;
import com.reggie.takeout.service.SetmealDishService;
import com.reggie.takeout.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐和套餐内的菜品信息
     * @param setmealDto
     */
    @Transactional
    public void saveSetmealWithDish(SetmealDto setmealDto) {

        this.save(setmealDto); // 新增基本套餐信息

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();

        setmealDishList = setmealDishList.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId()); // 设置套餐Id
            return setmealDish;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishList); // 批量新增套餐菜品信息
    }

    /**
     * 更新套餐和套餐内的菜品信息
     * @param setmealDto
     */
    @Transactional
    public void updateSetmealWithDish(SetmealDto setmealDto) {

        this.updateById(setmealDto); // 根据Id更新套餐基本信息

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();

        setmealDishList.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId()); // 设置套餐Id

            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, setmealDish.getSetmealId()); // 查询条件：setmeal_id
            queryWrapper.eq(SetmealDish::getDishId, setmealDish.getDishId()); // 查询条件：dish_id

            SetmealDish one = setmealDishService.getOne(queryWrapper); // 查询当前菜品信息

            // 判断是否能查到当前菜品信息
            if (one != null) {
                setmealDish.setId(one.getId()); // 设置setmealId
                setmealDishService.updateById(setmealDish); // 根据Id更新套餐菜品信息

            }
            else {
                setmealDishService.save(setmealDish); // 新增套餐菜品信息
            }
        });
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public SetmealDto getSetmealWithDish(Long id) {

        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> dishList = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(dishList);

        return setmealDto;
    }
}
