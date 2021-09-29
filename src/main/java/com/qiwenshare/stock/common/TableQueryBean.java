package com.qiwenshare.stock.common;

import lombok.Data;

@Data
public class TableQueryBean {
    //key, pageIndex, pageSize, sortField, sortOrder

    private Integer page;
    private Integer limit;
    private Integer beginCount;

    /**
     * 搜索关键词
     */
    private String key;

    /**
     * 排序字段
     */
    private String field;
    /**
     * 排序规则
     */
    private String order;



}
