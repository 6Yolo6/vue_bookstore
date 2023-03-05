package com.example.bookstore.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookstore.entity.User;
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
public interface UserService extends IService<User> {

    User getByName(String name);

    Boolean add(User user) throws Exception ;

    List<User> getByUserIds(String ids);

    Page<User> getAllUser(Integer pageNum, Integer pageSize);

    User updateUser(User user) throws Exception;

    void deleteByIds(String ids);
}
