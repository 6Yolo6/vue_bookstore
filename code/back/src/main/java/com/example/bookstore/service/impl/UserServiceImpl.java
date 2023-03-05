package com.example.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookstore.entity.Book;
import com.example.bookstore.mapper.UserMapper;
import com.example.bookstore.service.UserService;
import com.example.bookstore.entity.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookstore.utils.JwtUtil;
import com.example.bookstore.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private HttpServletRequest request;

    @Override
    public User getByName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", name);
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean add(User user) throws Exception  {
        //salt
        System.out.println(user);
        String salt = UUID.randomUUID().toString();
        user.setSalt(salt);
        String password = MD5Util.getEncode(user.getPassword(), salt);
        user.setPassword(password);
        user.setPrivilege(0);
        this.save(user);
        return true;
    }

    @Override
    public List<User> getByUserIds(String ids) {
        List<Integer> list = new ArrayList<>();
        String[] array = ids.split(",");

        for (String id:array) {
            list.add(Integer.valueOf(id));
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("username", "nickname", "id").in("id", list);
        return this.list(queryWrapper);
    }

    @Override
    public Page<User> getAllUser(Integer pageNum, Integer pageSize) {

        Page<User> page = new Page<>(pageNum, pageSize);
        return this.page(page);
    }

    @Override
    public User updateUser(User user) throws Exception {
//        String token = request.getHeader("Authorization");
////        System.out.println("token" + token);
//        String userId = JwtUtil.validateToken(token);
        System.out.println(user);
        User isUser = this.getById(user.getId());
        System.out.println(isUser);
        if (user.getPassword() != "") {//重置密码
            String salt = UUID.randomUUID().toString();
            isUser.setSalt(salt);
            String password = MD5Util.getEncode(user.getPassword(), salt);
            isUser.setPassword(password);
        }
        isUser.setNickname(user.getNickname());
        isUser.setTelephone(user.getTelephone());
        isUser.setAddress(user.getAddress());
        isUser.setUsername(user.getUsername());
        this.updateById(isUser);

        return isUser;
    }

    @Override
    public void deleteByIds(String ids) {
            List<String> listIds = new ArrayList<>();
            String[] aryIds = ids.split(",");
            for(String id: aryIds){
                listIds.add(id);
            }
            this.removeByIds(listIds);
    }
}
