package com.example.bookstore.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookstore.entity.Comment;
import com.example.bookstore.entity.Order;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CommentService;
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
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CommentService commentService;

    //添加
    @PostMapping("/add")
    public Result add(Integer bookId, Integer rate, String content){
        Result result = new Result();

        Comment comment = commentService.add(bookId, rate, content);
        result.setData(comment);
        result.success("评价成功");

        return result;
    }
    //追评
    @PostMapping("/append")
    public Result append(Integer bookId, String appendContent){
        Result result = new Result();

        Comment comment = commentService.append(bookId, appendContent);
        result.setData(comment);
        result.success("追评成功");

        return result;
    }
    //删除
    @PostMapping("/deleteByIds")
    public Result deleteByIds(String ids){
        Result result = new Result();

        commentService.deleteByIds(ids);
        result.success("删除成功");

        return result;
    }


    @GetMapping("/getAll")
    public Result getAll(Integer pageNum, Integer pageSize){
        Result result = new Result();
        Page<Comment> page = commentService.getAllComment(pageNum, pageSize);
        for (Comment comment:page.getRecords()) {
            comment.put("nickname", userService.getById(comment.getUserId()).getNickname());
            comment.put("bookName", bookService.getById(comment.getBookId()).getName());
        }
        result.setData(page);
        result.success("查询成功");
        return result;
    }

    //根据userId
    @GetMapping( "/getByUserId")
    public Result getByBookId() {
        Result result = new Result();
        //通过id查找
        List<Comment> list = commentService.getByUserId();
//        System.out.println(list);
        for (Comment comment:list) {
            comment.put("book", bookService.getById(comment.getBookId()));
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

    //根据bookId查评价
    @GetMapping( "/getByBookId")
    public Result getByBookId(Integer bookId) {
        Result result = new Result();
        //通过id查找
        List<Comment> list = commentService.getByBookId(bookId);
//        System.out.println(list);
        for (Comment comment:list) {
            comment.put("nickname", userService.getById(comment.getUserId()).getNickname());
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

}

