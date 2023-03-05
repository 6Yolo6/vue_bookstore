package com.example.bookstore.controller;


import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Favor;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CategoryService;
import com.example.bookstore.service.FavorService;
import com.example.bookstore.service.UserService;
import com.example.bookstore.utils.JwtUtil;
import com.example.bookstore.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/favor")
public class FavorController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FavorService favorService;

    //添加
    @PostMapping("/addById")
    public Result addById(Integer bookId){
        Result result = new Result();
        //查询该用户收藏列表是否存在该书籍
//        System.out.println("bookId" + bookId);
//        String token = request.getHeader("Authorization");
////        System.out.println("token" + token);
//        String userId = JwtUtil.validateToken(token);
        List<Favor> isExit = favorService.getFavorByUserId();
//        System.out.println(isExit);
        Boolean flag = false;
        for (Favor favor: isExit) {
            if (favor.getBookId() == bookId) {
                flag = true;
                break;
            }
        }
        //已收藏，取消收藏
        if (flag) {
            result.addError("已收藏");
        }
        else {
            Favor favor = favorService.add(bookId);
            result.setData(favor);
            result.success("收藏成功");
        }

        return result;
    }

    //根据id查收藏列表
    @GetMapping( "/getByUserId")
    public Result getByUserId() {
        Result result = new Result();
        //通过id查找
        List<Favor> list = favorService.getFavorByUserId();
//        System.out.println(list);
        Book book;
        for (Favor favor:list) {
            book = bookService.getById(favor.getBookId());
            favor.put("book", book);
            favor.put("cateName", categoryService.getCateById(book.getCategory()).getName());
        }
        if (!list.isEmpty()) {
            result.setData(list);
            result.success("查询成功");
        }
        else {
            result.fail("查询失败");
        }
        return result;
    }

    //取消收藏
    @PostMapping("/deleteByIds")
    public Result deleteByIds(String ids){
        Result result = new Result();

        favorService.deleteByIds(ids);
        result.success("删除成功");
        return result;
    }
}

