package com.example.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookstore.entity.Category;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.service.BookService;
import com.example.bookstore.entity.Book;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Override
    public Page<Book> getBookByCate(Integer pageNum, Integer pageSize, Integer category) {
//        System.out.println(pageNum + " " + pageSize);
        Page<Book> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        if (category == 0) {
            return this.page(page, queryWrapper);
        }
        queryWrapper.eq("category", category);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Book> getBookByIds(String ids) {

        List<Integer> list = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();
        String[] array = ids.split(",");
        for (String i:array) {
//            list.add(Integer.valueOf(i));
            QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("name", "author", "price", "img", "publisher").eq("id", Integer.valueOf(i));
            bookList.add(this.getOne(queryWrapper));
        }

//        queryWrapper.select("name", "author", "price", "img").in("id", list);

//        System.out.println(this.list(queryWrapper));
        return bookList;
    }

    @Override
    public Page<Book> searchBookByKey(Integer pageNum, Integer pageSize, Integer option, String key) {
        Page<Book> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        System.out.println(option + " " + key);
        if (option == 1) {//书名
            queryWrapper.like("name", key);
        }
        else if (option == 2) {//作者
            queryWrapper.like("author", key);
        }
        else if (option == 3) {//出版社
            queryWrapper.like("publisher", key);
        }
        return this.page(page, queryWrapper);
    }

    @Override
    public Book update(Book book) {
        this.updateById(book);
        return book;
    }

    @Override
    public Book getByName(String name) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.eq("name", name);
        return this.getOne(bookQueryWrapper);
    }

    //根据Id删除书籍
    public void deleteByIds(String ids) {
        List<String> list = new ArrayList<>();
        String[] array = ids.split(",");
        for (String i:array) {
            list.add(i);
        }
        this.removeByIds(list);
    }

}
