package com.example.bookstore.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CategoryService;
import com.example.bookstore.service.FavorService;
import com.example.bookstore.utils.Result;
import com.example.bookstore.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
//@CrossOrigin(origins="http://localhost:8080") //跨域
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private FavorService favorService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getAll")
    public Result getAll(){
        Result result = new Result();
        result.setData(bookService.list());
        result.success("查询成功");
        return result;
    }

    //根据id查询Book
    @GetMapping("/getBookById")
    public Result getBookById(Integer id){
        Result result = new Result();
        Book book = bookService.getById(id);
        book.put("cateName",categoryService.getCateById(book.getCategory()).getName());
        book.put("isFavor", favorService.getFavor(book.getId()) == null ? 0 : 1);
        result.setData(book);

        result.success("查询成功");
        return result;
    }

    //根据key搜索Book
    @GetMapping("/searchBookByKey")
    public Result searchBookByKey(Integer pageNum, Integer pageSize, Integer option, String key){
        Result result = new Result();
        Page<Book> book = bookService.searchBookByKey(pageNum, pageSize, option, key);
//        System.out.println(book.getRecords());
        return getBook(result, book);
    }

    //查根据id询cartbook
    @GetMapping("/getBookByIds")
    public Result getBookByIds(String ids){
//        System.out.println("ids:"+ids);
        Result result = new Result();
        List<Book> list = bookService.getBookByIds(ids);
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

    //根据category查询Book
    @GetMapping( "/getBookByCate")
    public Result getBookByCate(Integer pageNum, Integer pageSize, Integer category) {
        System.out.println("cate" + category);
        Result result = new Result();
        //通过id查找
        Page<Book> book = bookService.getBookByCate(pageNum, pageSize, category);
        return getBook(result, book);
    }

    private Result getBook(Result result, Page<Book> book) {
        for (int i=0; i < book.getRecords().size(); i++) {
            book.getRecords().get(i).put("cateName",categoryService.getCateById(book.getRecords().get(i).getCategory()).getName());
            book.getRecords().get(i).put("isFavor", favorService.getFavor(book.getRecords().get(i).getId()) == null ? 0 : 1);
        }
        result.setData(book);
        result.success("查询成功");
        return result;
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Book book){
        Result result = new Result();

        //查询数据库是否存在该书书籍
        Book isExit = bookService.getByName(book.getName());
        if (isExit != null)
            result.fail("书籍：" + book.getName() + "已存在");
        else {
            result.setData(bookService.save(book));
            result.success("添加成功");
        }
        return result;
    }

    //上传图片
    @PostMapping("/uploadImg")
    public String uploadImage(@RequestParam(value = "img") MultipartFile multipartFile){
        if (multipartFile.isEmpty()){
            return "文件有误！";
        }
        return UploadUtils.uploadImage(multipartFile);
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Book book){
        Result result = new Result();
        Book isExit = bookService.update(book);
//        System.out.println(isExit);
        if (isExit != null) {
            result.setData(isExit);
            result.success("更新成功");
        }
        else {
            result.fail("更新失败");
        }
        return result;
    }

    //根据ids删除
    @PostMapping("/deleteByIds")
    public Result deleteByIds(String ids){
        Result result = new Result();

        bookService.deleteByIds(ids);
        result.success("删除成功");
        return result;
    }
}

