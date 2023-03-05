package com.example.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookstore.entity.Comment;
import com.example.bookstore.entity.User;
import com.example.bookstore.mapper.CommentMapper;
import com.example.bookstore.mapper.FavorMapper;
import com.example.bookstore.service.CommentService;
import com.example.bookstore.service.FavorService;
import com.example.bookstore.utils.DateTool;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private HttpServletRequest request;

    @Override
    public List<Comment> getByBookId(Integer bookId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);

        return this.list(queryWrapper);
    }

    @Override
    public Comment add(Integer bookId, Integer rate, String content) {
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        Comment comment = new Comment();
//        System.out.println(userId);
        comment.setCreateTime(DateTool.getCurrTime());
        comment.setUserId(Integer.valueOf(userId));
        comment.setBookId(bookId);
        comment.setRate(rate);
        comment.setContent(content);
        this.save(comment);
        return comment;
    }

    @Override
    public List<Comment> getByUserId() {
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Integer.valueOf(userId));

        return this.list(queryWrapper);
    }

//
    @Override
    public void deleteByIds(String ids) {//根据Id删除
//        System.out.println(ids);
        List<String> list = new ArrayList<>();
        String[] array = ids.split(",");
        for (String i:array) {
            list.add(i);
        }
        this.removeByIds(list);
    }

    @Override
    public Page<Comment> getAllComment(Integer pageNum, Integer pageSize) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        return this.page(page);
    }

    @Override
    public Comment append(Integer bookId, String appendContent) {
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        Comment comment = this.getOne(queryWrapper.eq("user_id", Integer.valueOf(userId)).eq("book_id", bookId));
//        System.out.println(userId);
        comment.setAppendContent(appendContent);
        comment.setAppendTime(DateTool.getCurrTime());
        this.updateById(comment);
        return comment;
    }
}
