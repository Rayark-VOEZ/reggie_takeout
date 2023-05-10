package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.common.R;
import com.reggie.takeout.dto.DishDto;
import com.reggie.takeout.entity.Dish;
import com.reggie.takeout.service.CategoryService;
import com.reggie.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 更新菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<Object> updateDish(@RequestBody DishDto dishDto) {

        log.info("========== 更新菜品信息 ==========");

        // todo: 口味不能删除
        dishService.updateWithFlavor(dishDto);

        return R.success("修改成功！");
    }

    /**
     * 分页查询菜品信息
     * @param page 当前分页数
     * @param pageSize 分页大小
     * @param name 菜品名称
     * @return
     */
    @GetMapping("/page")
    public R<Object> queryByPage(int page, int pageSize, String name) {

        log.info("========== 分页查询菜品信息 ==========");

        Page<Dish> dishPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getId);

        // 查询分页数据
        dishService.page(dishPage, queryWrapper);

        List<DishDto> dishDtoList = new ArrayList<>();

        // 依次查询菜品的分类名
        dishPage.getRecords().forEach((Dish dish) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCategoryName(categoryService.getById(dish.getCategoryId()).getName());
            dishDtoList.add(dishDto);
        });

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage);
        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage);
    }

    /**
     * 查询菜品信息列表
     * @return
     */
    @GetMapping("/list")
    public R<Object> queryByList(Long categoryId) {

        log.info("========== 查询菜品信息列表 ==========");

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);

        return R.success(dishService.list(queryWrapper));
    }

    /**
     * 批量删除菜品
     * @param ids 菜品id字符串，以全角逗号分隔
     * @return R
     */
    @DeleteMapping
    public R<Object> deleteDish(String ids) {

        log.info("========== 批量删除菜品 ==========");

        List<Dish> dishList = new ArrayList<>();

        String[] dishIds = ids.split(","); // 把id字符串分割成id数组

        for (String dishId : dishIds) {
            Dish dish = new Dish();
            dish.setId(Long.valueOf(dishId));
            dishList.add(dish);
        }

        if (dishService.removeBatchByIds(dishList)) { // 根据id从数据库批量删除菜品
            return R.success("删除成功！");
        }
        else {
            return R.error("删除失败！");
        }
    }

    /**
     * 批量起售/停售菜品
     * @param status 菜品状态：0-停售，1-起售
     * @param ids 菜品id字符串，以全角逗号分隔
     * @return
     */
    @PostMapping("/status/{status}")
    public R<Object> batch(@PathVariable Integer status, String ids) {

        log.info("========== 批量起售/停售菜品 ==========");

        String[] dishIds = ids.split(",");
        List<Dish> dishList = new ArrayList<>();
        Arrays.stream(dishIds).forEach((String dishId) -> {
            Dish dish = new Dish();
            dish.setId(Long.valueOf(dishId));
            dish.setStatus(status);
            dishList.add(dish);
        });

        if (dishService.updateBatchById(dishList)) {
            return R.success("成功！");
        }
        else {
            return R.error("失败！");
        }
    }

    /**
     * 新增菜品信息
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<Object> save(@RequestBody DishDto dishDto) {

        log.info("========== 新增菜品信息 ==========");

        // todo: 处理图片上传

        dishService.saveWithFlavor(dishDto);

        return R.success("success");
    }

    /**
     * 根据id查询菜品信息
     * @param id 菜品id
     * @return
     */
    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable Long id) {

        log.info("========== 根据id查询菜品信息 ==========");

        DishDto dishDto = dishService.getByIdWithFlavour(id);

        if (dishDto != null) {
            return R.success(dishDto);
        }
        else {
            return R.error("Error!");
        }
    }
}
