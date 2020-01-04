package com.jiangbo.savior.builder.group;

public class GroupBy {

    private String column;

    public String getColumn() {
        return column;
    }

    private GroupBy(String column) {
        this.column = column;
    }

    public static GroupBy by(String column) {
        return new GroupBy(column);
    }
}
