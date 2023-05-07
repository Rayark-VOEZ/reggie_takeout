package com.reggie.takeout.controller;

import com.reggie.takeout.common.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    /**
     * 修改
     * @return
     */
    @PutMapping
    public R<Object> update() {
        return null;
    }

    /**
     *
     * @return
     */
    @GetMapping("/page")
    public R<Object> page() {
        return null;
    }

    /**
     * 列表
     * @return
     */
    @GetMapping("/list")
    public R<Object> list() {
        return null;
    }

    /**
     * 删除
     * @return
     */
    @DeleteMapping
    public R<Object> delete() {
        return null;
    }

    /**
     * 新增
     * @return
     */
    @PostMapping
    public R<Object> save() {
        return null;
    }

    /**
     * 查询
     * @param addressBookId
     * @return
     */
    @GetMapping("/{id}")
    public R<Object> getById(@PathVariable Long addressBookId) {
        return null;
    }

    /**
     * 查询最后更新地址
     * @return
     */
    @GetMapping("/lastUpdate")
    public R<Object> lastUpdate() {
        return null;
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public R<Object> getDefaultAddress() {
        return null;
    }

    // @PutMapping("/default")
    // public R<Object> default() {
    //     return null;
    // }
}
