package com.jiangbo.savior.callback;

/**
 * 数据库兼容方法
 * @param <T>
 */
public interface ICompatible<T> {

    public T executeMysql();


    public T executeOracel();
}
