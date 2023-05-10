package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.common.R;
import com.reggie.takeout.dto.SetmealDto;
import com.reggie.takeout.entity.Setmeal;
import com.reggie.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    /**
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<Object> updateSetmeal(@RequestBody SetmealDto setmealDto) {

        log.info("========== 更新套餐信息 ==========");

        setmealService.updateSetmealWithDish(setmealDto);

        return R.success("更新成功！");
    }

    /**
     * 分页查询套餐信息
     * @param page 当前分页
     * @param pageSize 分页大小
     * @param name 套餐名称
     * @return
     */
    @GetMapping("/page")
    public R<Object> queryByPage(int page, int pageSize, String name) {

        log.info("========== 分页查询套餐信息 ==========");

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        queryWrapper.orderByAsc(Setmeal::getId);

        setmealService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }


    /**
     * 查询套餐信息列表
     * @param categoryId 分类id
     * @return
     */
    @GetMapping("/list")
    public R<Object> list(Long categoryId) {

        log.info("========== 查询套餐信息列表 ==========");

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId);

        return R.success(setmealService.list(queryWrapper));
    }

    /**
     * 删除套餐信息
     * @param ids 套餐id字符串，以全角逗号分隔
     * @return
     */
    @DeleteMapping
    public R<Object> delete(String ids) {

        log.info("========== 删除套餐信息 ==========");

        System.out.println(ids);

        List<Setmeal> setmealList = new ArrayList<>();

        String[] setmealIds = ids.split(",");

        for (String setmealId : setmealIds) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(Long.valueOf(setmealId));
            setmealList.add(setmeal);
        }

        if (setmealService.removeBatchByIds(setmealList)) {
            return R.success("成功！");
        }
        else {
            return R.error("失败！");
        }
    }

    /**
     * 批量起售/停售套餐
     * @param status 套餐状态：0-停售，1-起售
     * @param ids 套餐id字符串，以全角逗号分隔
     * @return
     */
    @PostMapping("/status/{status}")
    public R<Object> batch(@PathVariable int status, String ids) {

        log.info("========== 批量起售/停售套餐 ==========");

        List<Setmeal> setmealList = new ArrayList<>();

        String[] setmealIds = ids.split(",");

        for (String setmealId : setmealIds) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(Long.valueOf(setmealId));
            setmeal.setStatus(status);
            setmealList.add(setmeal);
        }

        if (setmealService.updateBatchById(setmealList)) {
            return R.success("成功！");
        }
        else {
            return R.error("失败！");
        }
    }

    /**
     * 新增套餐信息
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<Object> saveSetmeal(@RequestBody SetmealDto setmealDto) {

        log.info("========== 新增菜品信息 ==========");

        setmealService.saveSetmealWithDish(setmealDto);

        return R.success("新增成功！");
    }

    /**
     * 根据id查询套餐信息
     * @return
     */
    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable Long id) {

        SetmealDto setmealDto = setmealService.getSetmealWithDish(id);

        if (setmealDto != null) {
            return R.success(setmealDto);
        }
        else {
            return R.error("Error!");
        }
    }

    @GetMapping("/dish/{id}")
    public R<Object> queryAllDish() {
        return null;
    }
}
