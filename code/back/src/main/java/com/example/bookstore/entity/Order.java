package com.example.bookstore.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


/**
 * <p>

 */
@Data
@TableName("`order`")
@ApiModel(value = "Order实体", description = "订单表")
public class Order extends BaseEntity implements Delayed {
    @ApiModelProperty(value = "订单编号")
    private String no;
    @ApiModelProperty(value = "订单用户id")
    private Integer userId;
    @ApiModelProperty(value = "订单总价")
    private Double totalPrice;
    @ApiModelProperty(value = "订单状态")
    private Integer status;
    @ApiModelProperty(value = "订单创建时间")
    private String createTime;
    @ApiModelProperty(value = "订单取消时间，DelayQueue使用")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date cancelTime;
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(cancelTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return cancelTime.compareTo(((Order) o).getCancelTime());
    }
}
