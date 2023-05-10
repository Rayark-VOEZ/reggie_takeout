package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.takeout.common.R;
import com.reggie.takeout.entity.User;
import com.reggie.takeout.service.UserService;
import com.reggie.takeout.util.SMSUtils;
import com.reggie.takeout.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 新增用户信息
     * @return
     */
    @PostMapping
    public R<Object> saveUser() {
        return null;
    }

    /**
     * 用户登出
     * @return
     */
    @PostMapping("/loginout")
    public R<Object> logout() {

        log.info("========== 用户登出 ==========");

        return null;
    }

    /**
     * 用户登录
     * @return
     */
    @PostMapping("/login")
    public R<Object> login(HttpSession session, @RequestBody Map map) {

        log.info("========== 用户登录 ==========");

        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        Object codeInSession = session.getAttribute(phone);

        if (codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);

            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }

            session.setAttribute("userId", user.getId());

            return R.success("登录成功！");
        }

        return R.error("登录失败！");
    }

    /**
     * 发送验证码短信
     * @param session HttpSession
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpSession session, @RequestBody User user) {

        log.info("========== 发送验证码短信 ==========");

        String phone = user.getPhone();

        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        // SMSUtils.sendMessage("菩提阁", "", phone, code);
        session.setAttribute(phone, code);

        return R.success(code);
    }

    /**
     * 更新用户信息
     * @return
     */
    @PutMapping
    public R<Object> updateUser() {
        return null;
    }

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    @DeleteMapping
    public R<Object> deleteUser(Long id) {
        return null;
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable Long id) {
        return null;
    }

    /**
     * 查询用户信息列表
     * @return
     */
    @GetMapping("/list")
    public R<Object> queryByList() {
        return null;
    }

    /**
     * 分页查询用户信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Object> queryByPage(int page, int pageSize) {
        return null;
    }
}
