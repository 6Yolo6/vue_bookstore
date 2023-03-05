package com.example.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
@Data
@TableName("user")
public class User extends BaseEntity {

    private String username;

    private String nickname;

    private String password;

    private Integer privilege;

    private String salt;

    private String telephone;

    private String address;
//    @Override
//    public String toString() {
//        return "User{" +
//        "id=" + id +
//        ", username=" + username +
//        ", password=" + password +
//        ", privilege=" + privilege +
//        "}";
//    }
}
