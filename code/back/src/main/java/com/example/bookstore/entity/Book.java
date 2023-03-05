package com.example.bookstore.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("book")
public class Book extends BaseEntity {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "出版社")
    private String publisher;
    @ApiModelProperty(value = "价格")
    private Float price;
    @ApiModelProperty(value = "图片url")
    private String img;
    @ApiModelProperty(value = "简介")
    private String description;
    @ApiModelProperty(value = "类别")
    private Integer category;




//    @Override
//    public String toString() {
//        return "Book{" +
//        "id=" + id +
//        ", name=" + name +
//        ", author=" + author +
//        ", publisher=" + publisher +
//        ", price=" + price +
//        ", img=" + img +
//        ", description=" + description +
//        ", category=" + category +
//        "}";
//    }
}
