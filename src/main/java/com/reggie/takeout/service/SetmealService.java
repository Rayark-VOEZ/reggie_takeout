package com.reggie.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.takeout.dto.SetmealDto;
import com.reggie.takeout.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    void saveSetmealWithDish(SetmealDto setmealDto);
    void updateSetmealWithDish(SetmealDto setmealDto);
    SetmealDto getSetmealWithDish(Long id);
}
