package com.joezhou.app.controller;

import com.joezhou.app.entity.User;
import com.joezhou.app.service.UserService;
import com.joezhou.app.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 添加用户记录
     * cli: http://localhost:8081/api/user/insert?username=a&
     *
     * @param user 用户实体
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("insert")
    public JsonResult insert(User user) {
        log.info("接收到添加用户记录请求，参数user：{}", user);
        return userService.save(user) ? JsonResult.ok() : JsonResult.fail();
    }

    /**
     * 按主键查询用户记录
     * cli: http://localhost:8081/api/user/select-by-id?id=1
     *
     * @param id 用户记录主键
     * @return 成功返回对应用户记录，失败返回失败信息
     */
    @RequestMapping("select-by-id")
    public JsonResult selectById(@RequestParam("id") Integer id) {
        log.info("接收到按主键查询用户记录请求，参数id：{}", id);
        User user = userService.getById(id);
        log.info("根据主键查询到用户记录：{}", user);
        return user != null ? JsonResult.ok(user) : JsonResult.fail();
    }

    /**
     * 按主键修改用户记录
     * cli: http://localhost:8081/api/user/update-by-id?id=1&...
     *
     * @param user 用户实体
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("update-by-id")
    public JsonResult updateById(User user) {
        log.info("接收到按主键修改用户记录请求，参数user：{}", user);
        return userService.updateById(user) ? JsonResult.ok() : JsonResult.fail();
    }

    /**
     * 按主键删除用户记录
     * cli: http://localhost:8081/api/user/delete-by-id?id=1
     *
     * @param id 用户记录主键
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("delete-by-id")
    public JsonResult deleteById(@RequestParam Integer id) {
        log.info("接收到按主键删除用户记录请求，参数id：{}", id);
        return userService.removeById(id) ? JsonResult.ok() : JsonResult.fail();
    }

    /**
     * 全查用户记录
     * cli: http://localhost:8081/api/user/list
     *
     * @return 成功返回全部用户记录，失败返回失败信息
     */
    @RequestMapping("list")
    public JsonResult list() {
        log.info("接收到按全查用户记录的请求");
        List<User> users = userService.list();
        log.info("全查到全部用户记录：{}", users);
        return users != null && !users.isEmpty() ? JsonResult.ok(users) : JsonResult.fail();
    }

}

