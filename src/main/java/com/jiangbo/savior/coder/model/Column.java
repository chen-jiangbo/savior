package com.jiangbo.savior.coder.model;

import com.jiangbo.savior.utils.StringUtils;

public class Column {

    private String columnType;

    private String javaType;

    private String columnDesc;

    private String columnName;

    private String propName;

    private String methodName;

    private Integer digits;

    private Integer columnSize;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getJavaSimpleTyep() {
        if (StringUtils.equals("java.lang.String", this.javaType)) {
            return "String";
        } else if (StringUtils.equals("java.util.Date", this.javaType)) {
            return "Date";
        } else if (StringUtils.equals("java.lang.Integer", this.javaType)) {
            return "Integer";
        } else if (StringUtils.equals("java.lang.Float", this.javaType)) {
            return "Float";
        } else if (StringUtils.equals("java.lang.Double", this.javaType)) {
            return "Double";
        } else if (StringUtils.equals("java.math.BigDecimal", this.javaType)) {
            return "BigDecimal";
        } else if (StringUtils.equals("java.lang.Long", this.javaType)) {
            return "Long";
        } else {
            return this.javaType;
        }
    }


    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getColumnDesc() {
        return columnDesc == null ? "" : columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }
}
