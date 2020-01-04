package com.jiangbo.savior.model;

import java.util.HashMap;
import java.util.Map;

public class Context extends Container {

    public Context setColumn(String column, Object val) {
        super.getData().put(column, val);
        return this;
    }
    @Override
    public Map<String, Object> initData() {
        return new HashMap<>();
    }

    public <T> T getObject(String key,Class<T> clazz) {
        final Object o = get(key);
        if (o == null) {
            return null;
        }
        return (T) o;
    }
}
