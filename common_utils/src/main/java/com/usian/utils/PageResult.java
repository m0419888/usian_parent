package com.usian.utils;

import lombok.Data;

import java.util.List;

@Data
public class PageResult {
    private Integer pageIndex; //当前页
    private Long totalPage; //总页数
    private List result; //结果集
}
