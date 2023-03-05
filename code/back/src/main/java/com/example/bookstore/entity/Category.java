package com.example.bookstore.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
@TableName("category")
public class Category extends BaseEntity {

    private String name;



//    @Override
//    public String toString() {
//        return "Category{" +
//        "id=" + id +
//        ", name=" + name +
//        "}";
//    }
}
