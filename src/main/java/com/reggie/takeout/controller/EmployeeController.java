package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.common.R;
import com.reggie.takeout.entity.Employee;
import com.reggie.takeout.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Object> login(HttpSession session, @RequestBody Employee employee) throws NoSuchAlgorithmException {

        // 1、将页面提交的密码password进行md5加密处理
        // MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        // byte[] digest = messageDigest.digest(employee.getPassword().getBytes(StandardCharsets.UTF_8));
        String md5 = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes(StandardCharsets.UTF_8));

        // 2、根据页面提交的用户名username查询数据库
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", employee.getUsername());
        Employee e = employeeService.getOne(queryWrapper);

        // 3、如果没有查询到则返回登录失败结果
        if (e == null) {
            return R.error("登录失败！");
        }

        // 4、密码比对，如果不一致则返回登录失败结果
        if (!e.getPassword().equals(md5)) {
            return R.error("登录失败！");
        }

        // 5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (e.getStatus() == 0) {
            return R.error("员工已被禁用");
        }

        // 6、登录成功，将员工id存入Session并返回登录成功结果
        session.setAttribute("userId", e.getId());
        return R.success(e);
    }

    @PostMapping("/logout")
    public R<Object> logout(HttpSession session) {

        // 1、清理Session中的用户id
        session.removeAttribute("userId");

        // 2、返回结果
        return R.success(null);
    }

    /**
     * 新增用户
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<Object> save(HttpServletRequest request, @RequestBody Employee employee) {

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        //
        // Long userId = (Long) request.getSession().getAttribute("userId");
        //
        // employee.setCreateUser(userId);
        // employee.setUpdateUser(userId);

        try {
            employeeService.save(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("新增员工失败！");
        }

        return R.success("新增员工成功！");
    }


    /**
     * 分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Object> page(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, String name) {

        // 分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        // 条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行分页查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<Object> update(HttpServletRequest request, @RequestBody Employee employee) {

        Long userId = (Long) request.getSession().getAttribute("userId");

        // employee.setUpdateUser(userId);
        // employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return R.success("修改成功");
    }

    /**
     * 修改密码
     * @return
     */
    @PutMapping("/updatePassword")
    public R<Object> updatePassword() {
        return null;
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Object> getById(@PathVariable Long id) {

        Employee employee = employeeService.getById(id);

        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");
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
     * 删除用户
     * @return
     */
    @DeleteMapping
    public R<Object> delete() {
        return null;
    }
}
