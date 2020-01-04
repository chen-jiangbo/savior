package com.jiangbo.savior.model;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

public abstract class Container {

    private Map<String,Object> data;

    public Container(){
        this.data=initData();
    }


    public Object get(String key){
        return this.data.get(key);
    }

    public void put(String key,Object val){
         this.data.put(key,val);
    }

    public void remove(String key){
        this.data.remove(key);
    }

    public abstract Map<String,Object> initData();

    public void addAll(Map<String,Object> map) {
        this.data.putAll(map);
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    private Object getVal(String key) {
        return this.data.get(key);
    }

    private String getValToString(Object val) {
        if (val instanceof Integer) {
            return ((Integer) val).toString();
        } else if (val instanceof Long) {
            return ((Long) val).toString();
        } else if (val instanceof Double) {
            return ((Double) val).toString();
        } else if (val instanceof Float) {
            return ((Float) val).toString();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).toString();
        } else if (val instanceof BigDecimal) {
            return ((BigDecimal) val).setScale(4).toPlainString();
        }
        return null;
    }

    public String getString(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        }
        if (val instanceof String) {
            return (String) val;
        }
        return getValToString(val);
    }

    public Integer getInteger(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        }
        if (val instanceof Integer) {
            return (Integer) val;
        }
        return Integer.parseInt(getValToString(val));
    }

    public Boolean getBoolean(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        }
        if (val instanceof Boolean) {
            return (Boolean) val;
        }
        return Boolean.parseBoolean(getValToString(val));
    }

    public Double getDouble(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        }
        if (val instanceof Double) {
            return (Double) val;
        }
        return Double.parseDouble(getValToString(val));
    }

    public Float getFloat(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        }
        if (val instanceof Float) {
            return (Float) val;
        }
        return Float.parseFloat(getValToString(val));
    }

    public BigInteger getBigInteger(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        }
        if (val instanceof BigInteger) {
            return (BigInteger) val;
        }
        return BigInteger.valueOf(Long.parseLong(getValToString(val)));
    }

    public Long getLong(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        }
        if (val instanceof Long) {
            return (Long) val;
        }
        return Long.parseLong(getValToString(val));
    }

    public BigDecimal getBigDecimal(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        }
        if (val instanceof BigDecimal) {
            return (BigDecimal) val;
        }
        return BigDecimal.valueOf(Double.parseDouble(getValToString(val)));
    }

    public Date getDate(String key) {
        Object val = getVal(key);
        if (val == null) {
            return null;
        } else if (val instanceof Long) {
            return new Date((long) val);
        }
        return (Date) val;
    }
}
