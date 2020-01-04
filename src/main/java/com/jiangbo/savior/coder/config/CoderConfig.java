package com.jiangbo.savior.coder.config;

import javax.sql.DataSource;

public class CoderConfig {
    /**
     * 设置代码生成器数据连接池
     * 注:如果为空使用系统默认配置
     */
    private DataSource dataSource=null;
    /**
     * 是否生成swqgger注解
     */
    private boolean isGenSwaggerAnnotation=true;
    /**
     * 是否使用lombok注解
     */
    private boolean isSupportLombok=true;

    public boolean isGenSwaggerAnnotation() {
        return isGenSwaggerAnnotation;
    }

    public void setGenSwaggerAnnotation(boolean genSwaggerAnnotation) {
        isGenSwaggerAnnotation = genSwaggerAnnotation;
    }

    public boolean isSupportLombok() {
        return isSupportLombok;
    }

    public void setSupportLombok(boolean supportLombok) {
        isSupportLombok = supportLombok;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
