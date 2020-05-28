package com.jiangbo.savior.builder.limit;


import com.jiangbo.savior.adapter.DaoAdapter;

public class LimitBuilder {

    public static String addSubLimitSql(StringBuffer sf, Limit limit, DaoAdapter adapter) {
        if (null == limit) {
            return sf.toString();
        }
        return adapter.addLimit(sf, limit.getOffset(), limit.getSize());
    }
}
