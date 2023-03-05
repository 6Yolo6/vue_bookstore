package com.example.bookstore.controller;


import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Favor;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.UserService;
import com.example.bookstore.service.CartService;
import com.example.bookstore.utils.JwtUtil;
import com.example.bookstore.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
//@CrossOrigin(origins="http://localhost:8080") //跨域 允许来自localhost:8080的请求
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    //添加
    @PostMapping("/addById")
    public Result addById(Integer bookId){
        Result result = new Result();
//        System.out.println(bookId);

        Cart cart = cartService.add(bookId);
        result.setData(cart);
        result.success("添加成功");

        return result;
    }

    //减少
    @PostMapping("/subById")
    public Result subById(Integer bookId){
        Result result = new Result();
//        System.out.println(bookId);
        result.setData(cartService.sub(bookId));
        result.success("书籍数量-1");
        return result;
    }

    //根据id查购物车
    @GetMapping( "/getByUserId")
    public Result getByUserId() {
        Result result = new Result();
        //通过id查找
        List<Cart> list = cartService.getCartByUserId();
//        System.out.println(list);
        if (!list.isEmpty()) {
            result.setData(list);
            result.success("查询成功");
        }
        else {
            result.fail("查询失败");
        }
        return result;
    }

    //选中
    @PostMapping("/updateByIds")
    public Result updateByIds(String ids){
        Result result = new Result();
        cartService.updateCartByIds(ids);
        result.success("更新选中成功");
        return result;
    }

    //删除
    @PostMapping("/deleteByIds")
    public Result deleteByIds(String ids){
        Result result = new Result();

        cartService.deleteByIds(ids);
        result.success("删除成功");
        return result;
    }
}

