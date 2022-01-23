package com.qiwenshare.stock.constant;

/**
 * 股票任务类型枚举
 */
public enum StockTaskTypeEnum {
    STOCK_LIST(0, "股票列表任务"),
    STOCK_DETAIL(1, "股票详情任务");

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
