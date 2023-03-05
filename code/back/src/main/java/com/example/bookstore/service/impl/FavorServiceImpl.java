package com.example.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookstore.entity.Favor;
import com.example.bookstore.mapper.FavorMapper;
import com.example.bookstore.service.FavorService;
import com.example.bookstore.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
@Service
public class FavorServiceImpl extends ServiceImpl<FavorMapper, Favor> implements FavorService {
    @Autowired
    private HttpServletRequest request;
    @Override
    public Favor add(Integer bookId) {
        String token = request.getHeader("Authorization");
//        System.out.println("token" +token);
        String userId = JwtUtil.validateToken(token);
        Favor favor = new Favor();
        System.out.println(userId);
        favor.setUserId(Integer.valueOf(userId));
        favor.setBookId(bookId);
        this.save(favor);
        return favor;
    }

    @Override
    public Favor getFavor(Integer bookId) {
        String token = request.getHeader("Authorization");
        if (token == null)
            return null;
//        System.out.println("token" +token);
        String userId = JwtUtil.validateToken(token);
        QueryWrapper<Favor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId).eq("user_id", Integer.valueOf(userId));
        return this.getOne(queryWrapper);

    }

    @Override
    public void deleteByIds(String ids) {//根据Id删除书籍
//        System.out.println(ids);
        String token = request.getHeader("Authorization");
//        System.out.println("token" +token);
        String userId = JwtUtil.validateToken(token);

        QueryWrapper<Favor> queryWrapper = new QueryWrapper<>();
        List<String> list = new ArrayList<>();
        String[] array = ids.split(",");
        for (String i:array) {
            list.add(i);
        }
        queryWrapper.in("book_id", list).eq("user_id", Integer.valueOf(userId));
        this.remove(queryWrapper);
    }

    @Override
    public void update(Favor favor) {
        this.updateById(favor);
    }

    @Override
    public List<Favor> getFavorByUserId() {
        QueryWrapper<Favor> queryWrapper = new QueryWrapper<>();
        String token = request.getHeader("Authorization");
//        System.out.println("token" +token);
        String userId = JwtUtil.validateToken(token);
        queryWrapper.eq("user_id",Integer.valueOf(userId));

        return this.list(queryWrapper);
    }


}
