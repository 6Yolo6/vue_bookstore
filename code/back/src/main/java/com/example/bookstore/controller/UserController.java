package com.example.bookstore.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookstore.service.UserService;
import com.example.bookstore.utils.JwtUtil;
import com.example.bookstore.utils.MD5Util;
import com.example.bookstore.utils.Result;
import com.example.bookstore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
//@CrossOrigin(origins="http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;

    //register
    @PostMapping("/register")
    public Result register(@RequestBody User user) throws Exception {
        System.out.println(user);

        Result result = new Result();
        User isUser = userService.getByName(user.getUsername());
        System.out.println(isUser);
        if (isUser != null) {
            result.againLogin("该用户名已注册");
            return result;
        }
        else {
            Boolean flag = userService.add(user);
            if (flag) {
                result.success("注册成功");
            }
        }
        return result;
    }

    //login
    @PostMapping("/login")
    public Result login(@RequestBody User user) throws Exception {
        System.out.println(user);

        Result result = new Result();
        User isUser = userService.getByName(user.getUsername());
        System.out.println(isUser);

        if (isUser == null) {
            result.againLogin("用户名不正确");
            return result;
        }
        else {
            String password = MD5Util.getEncode(user.getPassword(), isUser.getSalt());
            if(password.equals(isUser.getPassword())) {
                result.success("登录成功");
                Map<String,Object> map = new HashMap<>();
                isUser.setSalt(null);
                isUser.setPassword(null);
                map.put("user", isUser);
                map.put("Authorization", JwtUtil.generateToken((isUser.getId()).toString()));
                result.setData(map);

            }
            else
                result.againLogin("密码错误");
        }

        return result;
    }

//      get
    @GetMapping("/getUser")
    public Result getUser(String username) {
        Result result = new Result();
        System.out.println(username);
        User user = userService.getByName(username);
        if (user == null) {
            result.fail("找不到该用户");
            return result;
        }
        user.setId(0);
        user.setPassword("");
        user.setSalt("");
        Map<String,Object> map = new HashMap<>();
        map.put("user", user);
        String token = JwtUtil.generateToken((user.getId()).toString());

        map.put("Authorization", token);

        result.success("获取成功");
        result.setData(map);

        return result;
    }

    @GetMapping("/getAllUser")
    public Result getAllUser(Integer pageNum, Integer pageSize) {
        Result result = new Result();
        Page<User> page = userService.getAllUser(pageNum, pageSize);
        List<User> user = page.getRecords();
        for (User user1:user) {
            user1.setPassword("");
            user1.setSalt("");
        }
        result.setData(page);
        result.success("查询成功");
        return result;
    }

    @GetMapping("/getByIds")
    public Result getByIds(String ids) {
        Result result = new Result();

        List<User> user = userService.getByUserIds(ids);
        if (user.isEmpty()) {
            result.fail("找不到该用户");
            return result;
        }

        result.setData(user);
        result.success("获取成功");

        return result;
    }

    @PostMapping("/deleteByIds")
    public Result deleteByIds(String ids) {
        Result result = new Result();

        userService.deleteByIds(ids);

        result.success("删除成功");

        return result;
    }

    //update地址电话
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        System.out.println(user);
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        Result result = new Result();
        User isUser = userService.getById(Integer.valueOf(userId));
//        System.out.println(isUser);

        if (isUser != null) {
            //添加地址、电话
            isUser.setTelephone(user.getTelephone());
            isUser.setAddress(user.getAddress());
            userService.updateById(isUser);
            System.out.println(userService.getById(isUser.getId()));
            result.setData(isUser);
            result.success("更新成功");
        }
        else {
            result.fail("用户不存在");
        }

        return result;
    }

    //update用户
    @PostMapping("/update")
    public Result update(@RequestBody User user) throws Exception {
//        System.out.println(user);

        Result result = new Result();

//        System.out.println(isUser);
        User isUser = userService.updateUser(user);
        if (isUser != null) {
            isUser.setPassword("");
            isUser.setSalt("");
            result.setData(isUser);
            result.success("更新成功");
        }
        else {
            result.fail("更新失败");
        }
        return result;
    }

    @GetMapping( "/validate")
    public Result validate() {
        String token = request.getHeader("Authorization");
        String userId = JwtUtil.validateToken(token);

        Result result = new Result();
        User user = userService.getById(userId);

        result.setData(user);
        if (user == null) {
            result.fail("验证失败");
            return result;
        }

        result.success("验证成功");

        return result;
    }


}



