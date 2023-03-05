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
@TableName("cart")
public class Cart extends BaseEntity {

    private Integer bookId;

    private Integer userId;

    private Integer num;

    private Integer checked;
}
