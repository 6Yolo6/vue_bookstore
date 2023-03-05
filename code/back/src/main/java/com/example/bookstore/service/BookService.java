package com.example.bookstore.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookstore.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
public interface BookService extends IService<Book> {
    Page<Book> getBookByCate(Integer pageNum, Integer pageSize, Integer category);
    void deleteByIds(String ids);
    List<Book> getBookByIds(String ids);

    Page<Book> searchBookByKey(Integer pageNum, Integer pageSize, Integer option, String key);

    Book update(Book book);

    Book getByName(String name);
}
