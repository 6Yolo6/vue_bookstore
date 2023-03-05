package com.example.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("favor")
public class Favor extends BaseEntity {

    private Integer bookId;

    private Integer userId;
}
