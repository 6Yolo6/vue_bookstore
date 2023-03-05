package com.example.bookstore.controller;


import com.example.bookstore.entity.Category;
import com.example.bookstore.service.CategoryService;
import com.example.bookstore.service.UserService;
import com.example.bookstore.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;



    @GetMapping( "/getAllCate")
    public Result getAllCate() {
        Result result = new Result();

        List<Category> isExit = categoryService.getAllCate();
//        System.out.println(isExit);
        if (isExit != null) {
            result.success("查询成功");
            result.setData(isExit);
        }
        else {
            result.fail("查询失败");
        }
        return result;
    }
}

