package com.jiangbo.savior.builder.where;

import java.util.List;

public class Logical {

    private String operation;//逻辑操作符(AND/OR)

    private List<Condition> conditionList;

    private Condition condition;

    public Logical(String operation, List<Condition> conditionList, Condition condition) {
        this.operation = operation;
        this.conditionList = conditionList;
        this.condition = condition;
    }

    public static Logical and(Condition condition){
        return new Logical(" AND ",null,condition);
    }

    public static Logical and(List<Condition> conditions){
        return new Logical(" AND ",conditions,null);
    }

    public static Logical or(Condition condition){
        return new Logical(" OR ",null,condition);
    }

    public static Logical or(List<Condition> conditions){
        return new Logical(" OR ",conditions,null);
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
