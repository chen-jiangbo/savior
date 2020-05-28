package com.jiangbo.savior.builder.where;


import com.jiangbo.savior.adapter.DaoAdapter;
import com.jiangbo.savior.utils.StringUtils;

public class Condition{
    private String name;// 条件名
    private String alias;//别名
    private String attr;//属性名
    private Opt Opertion;// 运算符
    private Object value;// 条件值

    private Condition() {
        super();
    }

    private Condition(Opt opertion, String name, Object value) {
        super();
        setOpertion(opertion);
        setName(name);
        setValue(value);
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public static Condition eq(String propertyName, Object value) {
        return new Condition(Opt.Equal, propertyName, value);
    }

    public static Condition uEq(String propertyName, Object value) {
        return new Condition(Opt.UnEqual, propertyName, value);
    }

    public static Condition greatThan(String propertyName, Object value) {
        return new Condition(Opt.GreatThan, propertyName, value);
    }

    public static Condition lessThan(String propertyName, Object value) {
        return new Condition(Opt.LessThan, propertyName, value);
    }

    public static Condition like(String propertyName, Object value) {
        return new Condition(Opt.Like, propertyName, value);
    }

    public static Condition in(String propertyName, Object value) {
        return new Condition(Opt.In, propertyName, value);
    }

    public static Condition notIn(String propertyName, Object value){
        return new Condition(Opt.NotIn, propertyName, value);
    }

    public static Condition isNull(String propertyName){
        return new Condition(Opt.IsNull, propertyName, null);
    }

    public static Condition isNotNull(String propertyName){
        return new Condition(Opt.IsNotNull, propertyName, null);
    }



    public static Condition appendSql(String sql){
        return new Condition(Opt.appendSql, null, sql);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Opt getOpertion() {
        return Opertion;
    }

    public void setOpertion(Opt opertion) {
        Opertion = opertion;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String name, DaoAdapter daoAdapter) {
        name=(name==null?"":name.replace(".","_").replace(daoAdapter.getSeparator(),""))+ StringUtils.uuid();
        setAttr(name);
        if (this.Opertion != null && (this.Opertion.equals(Opt.In)||this.Opertion.equals(Opt.NotIn))) {
            this.alias = "(:" + name + ") ";
        } else if(this.Opertion.equals(Opt.IsNull)){
            this.alias = " is null ";
        } else if(this.Opertion.equals(Opt.IsNotNull)){
            this.alias = " is not null ";
        } else {
            this.alias = ":" + name;
        }
    }
}
