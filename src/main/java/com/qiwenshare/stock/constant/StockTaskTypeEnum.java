package com.qiwenshare.stock.constant;

/**
 * 股票任务类型枚举
 */
public enum StockTaskTypeEnum {
    STOCK(0, "股票列表任务"),
    TIME(1, "分时线任务"),
    DAY(2, "日线任务"),
    WEEK(3, "周线任务"),
    MONTH(4, "月线任务");

    private int typeCode;

    private String typeName;

    StockTaskTypeEnum(int typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }


    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
