package com.example.bookstore.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookstore.entity.Comment;
import com.example.bookstore.entity.Favor;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
public interface CommentService extends IService<Comment> {
    List<Comment> getByBookId(Integer bookId);

    Comment add(Integer bookId, Integer rate, String content);

    List<Comment> getByUserId();
    void deleteByIds(String ids);

    Page<Comment> getAllComment(Integer pageNum, Integer pageSize);

    Comment append(Integer bookId, String appendContent);
}
