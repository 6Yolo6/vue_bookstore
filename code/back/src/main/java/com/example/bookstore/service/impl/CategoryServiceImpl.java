package com.example.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookstore.entity.Category;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getAllCate() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name");
//        System.out.println(this.list(queryWrapper));
        return this.list(queryWrapper);
    }

    @Override
    public Category getCateById(Integer id) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return this.getOne(queryWrapper);
    }


}
