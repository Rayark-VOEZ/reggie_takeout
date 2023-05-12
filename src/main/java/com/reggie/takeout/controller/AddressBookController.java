package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.takeout.common.BaseContext;
import com.reggie.takeout.common.R;
import com.reggie.takeout.entity.AddressBook;
import com.reggie.takeout.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 地址簿控制层
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 更新地址信息
     * @return
     */
    @PutMapping
    public R<Object> updateAddressBook(@RequestBody AddressBook addressBook) {

        log.info("========== 更新地址信息 ==========");

        addressBookService.updateById(addressBook);

        if (addressBookService.updateById(addressBook)) {
            return R.success("更新地址信息失败");
        }
        else {
            return R.error("更新地址信息成功");
        }
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
     * 查询地址列表
     * @return
     */
    @GetMapping("/list")
    public R<Object> queryByList() {

        log.info("========== 查询地址列表 ==========");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());

        return R.success(addressBookService.list(queryWrapper));
    }

    /**
     * 删除地址信息
     * @param ids 地址信息Id
     * @return
     */
    @DeleteMapping
    public R<Object> deleteAddress(Long ids) {

        log.info("========== 删除地址信息 ==========");

        AddressBook addressBook = addressBookService.getById(ids);

        addressBookService.removeById(ids); // 根据Id删除地址信息

        if (addressBook.getIsDefault() == 1) { // 如果被删的地址信息是默认地址
            LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
            queryWrapper.orderByDesc(AddressBook::getUpdateTime);
            queryWrapper.last("limit 1");

            AddressBook address = addressBookService.getOne(queryWrapper); // 查询最后修改的地址信息
            address.setIsDefault(1); // 设置为默认地址
            addressBookService.updateById(address); // 更新
        }

        return R.success("删除地址信息成功！");
    }

    /**
     * 新增地址信息
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<Object> saveAddress(@RequestBody AddressBook addressBook) {

        log.info("========== 新增地址信息 ==========");

        addressBook.setUserId(BaseContext.getCurrentId());

        if (addressBookService.save(addressBook)) {
            return R.success("新增地址成功！");
        }
        else {
            return R.error("新增地址失败！");
        }
    }

    /**
     * 根据Id查询地址信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable Long id) {

        log.info("========== 根据Id查询地址信息 ==========");

        AddressBook addressBook = addressBookService.getById(id);

        if (addressBook != null) {
            return R.success(addressBook);
        }
        else {
            return R.error(null);
        }
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
     * 查询默认地址信息-GET
     * @return
     */
    @GetMapping("/default")
    public R<Object> getDefaultAddress() {

        log.info("========== 查询默认地址信息 ==========");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (addressBook != null) {
            return R.success(addressBook);
        }
        else {
            return R.error(null);
        }
    }

    /**
     * 设置默认地址信息
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<Object> setDefaultAddress(@RequestBody AddressBook addressBook) {

        log.info("========== 设置默认地址信息 ==========");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());

        List<AddressBook> addressBookList = addressBookService.list(queryWrapper); // 获取全部地址信息

        addressBookList = addressBookList.stream().map(address -> { // 依次判断地址信息列表里的Id
            if (Objects.equals(address.getId(), addressBook.getId())) // 如果地址列表里的Id与请求的Id一致
                address.setIsDefault(1); // 设置为默认地址
            else
                address.setIsDefault(0);
            return address;
        }).collect(Collectors.toList());

        if (addressBookService.updateBatchById(addressBookList)) {
            return R.success("Success!");
        }
        else {
            return R.error("Error!");
        }
    }
}
