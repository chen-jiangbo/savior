package com.jiangbo.savior.builder.where;

public enum Opt {
    // 等于
    Equal,
    // 不等于
    UnEqual,
    // 大于
    GreatThan,
    // 小于
    LessThan,
    // 模糊like
    Like,
    // 范围查询
    In,
    // 范围查询
    notIn,
    //动态拼接SQL
    appendSql;
}
