package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.common.R;
import com.reggie.takeout.entity.Category;
import com.reggie.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<Object> save(@RequestBody Category category) {

        log.info("category: {}", category);

        try {
            categoryService.save(category);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.success("新增分类成功！");
    }

    /**
     * 分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Object> page(int page, int pageSize) {

        // 分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // 添加排序条件，根据sort排序
        queryWrapper.orderByAsc(Category::getSort);

        // 分页查询
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    public R<Object> update(@RequestBody Category category) {

        categoryService.updateById(category);

        return R.success("修改成功！");
    }

    /**
     * 列表
     * @return
     */
    @GetMapping("/list")
    public R<Object> queryByList(Category category) {

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != (category.getType()), Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        return R.success(categoryService.list(queryWrapper));
    }

    /**
     * 删除
     * @return
     */
    @DeleteMapping
    public R<Object> deleteCategory(Long ids) {

        Category category = new Category();
        category.setId(ids);
        category.setIsDeleted(1);

        return R.success(categoryService.updateById(category));
    }

    /**
     * 根据id查询分类信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Object> getById(@PathVariable Long id) {

        return R.success(categoryService.getById(id));
    }
}
