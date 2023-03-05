package com.example.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@TableName("comment")
@ApiModel(value = "comment实体", description = "评价表")
public class Comment extends BaseEntity {
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "书籍id")
    private Integer bookId;
    @ApiModelProperty(value = "评分")
    private Integer rate;
    @ApiModelProperty(value = "评价内容")
    private String content;
    @ApiModelProperty(value = "时间")
    private String createTime;
    @ApiModelProperty(value = "追评内容")
    private String appendContent;
    @ApiModelProperty(value = "追评时间")
    private String appendTime;
}
