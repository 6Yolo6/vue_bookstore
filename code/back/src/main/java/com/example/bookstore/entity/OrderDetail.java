package com.example.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author hahaha
 * @since 2022-11-27
 */
@Data
@TableName("order_detail")
public class OrderDetail extends BaseEntity {

    private Integer bookId;

    private Integer orderId;

    private Integer num;

    private Integer status;
}
