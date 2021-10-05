package com.joezhou.app.controller;

import com.joezhou.app.entity.User;
import com.joezhou.app.service.UserService;
import com.joezhou.app.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-15
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("select-by-id")
    public String selectById(@RequestParam("id") Integer id) {
        log.info("接收到一个查询请求，根据用户ID查询用户信息");
        User user = userService.getById(id);
        if (user == null) {
            return JacksonUtil.build(0, "用户查询失败");
        }
        log.info("查询请求执行成功，数据为 {}", user);
        return JacksonUtil.build(user);
    }

}

