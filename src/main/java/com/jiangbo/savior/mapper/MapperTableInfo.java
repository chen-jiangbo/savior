package com.jiangbo.savior.mapper;

import java.util.function.Supplier;

public class MapperTableInfo<T> {



    public MapperTableInfo(String tableName, Supplier<T> supplier, String primaryKey) {
        this.tableName = tableName;
        this.supplier = supplier;
        this.primaryKey = primaryKey;
    }

    /**
     * 表名
     */
    public String tableName;
    /**
     * 实体构造器
     */
    private  Supplier<T> supplier;

    /**
     * 主键名
     */
    private String primaryKey;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
