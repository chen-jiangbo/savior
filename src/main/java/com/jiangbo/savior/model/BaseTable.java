package com.jiangbo.savior.model;

import com.jiangbo.savior.exception.ReversalException;

import java.io.Serializable;

/**
 * 使用这个类时数据转换只需要实现其中的一个方法就可以了
 * 转换的性能很高
 */
public abstract class BaseTable implements Serializable {
    /**
     * dao传值上下文
     */
    protected Container context;

    /**
     * 初始化container
     */
    public void initContext(Container context) {
        this.context=context;
    }

    /**
     * (更新与插入时调用)
     * 对象转换成表记录字段
     * @param <T>
     * @return
     */
    public <T> Record reversal() {
        throw new ReversalException("对象转换的方法没有重写!");
    }

    /**
     * 表记录转换成对象
     *（查询时调用）
     * @param record
     */
    public abstract void convert(Record record);

    public void preCreate(Record record){
        return ;
    }

    public void postCreate(Record record){
        return ;
    }

    public void createTable(Record record) {
        preCreate(record);
        convert(record);
        postCreate(record);
    }

}
