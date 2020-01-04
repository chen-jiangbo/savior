package com.jiangbo.savior.builder;

import com.jiangbo.savior.adapter.DaoAdapter;
import com.jiangbo.savior.builder.group.GroupBuilder;
import com.jiangbo.savior.builder.group.GroupBy;
import com.jiangbo.savior.builder.having.HavingBuilder;
import com.jiangbo.savior.builder.order.OrderBuilder;
import com.jiangbo.savior.builder.order.OrderBy;
import com.jiangbo.savior.builder.where.Logical;
import com.jiangbo.savior.builder.having.Having;
import com.jiangbo.savior.builder.where.Condition;
import com.jiangbo.savior.builder.where.WhereBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * sql拼接的工具类
 */
public class SqlBuilder{
    private StringBuffer sql;
    private List<OrderBy> orderBys=new ArrayList<>();
    private List<GroupBy> groupBys=new ArrayList<>();
    private Having having=null;
    private List<Logical> list = new ArrayList<Logical>();

    private SqlBuilder() {
    }

    private SqlBuilder(String sql) {
        this.sql = new StringBuffer(sql);
    }

    public static SqlBuilder getSelect(String sql) {
        return new SqlBuilder(sql);
    }

    /**
     * @param condition
     * @类名称:and
     * @类描述:添加and条件
     */
    public SqlBuilder and(Condition condition) {
        this.list.add(Logical.and(condition));
        return this;
    }

    /**
     * @param conditions
     * @类名称:and
     * @类描述:添加and条件
     */
    public SqlBuilder andListOr(List<Condition> conditions) {
        this.list.add(Logical.and(conditions));
        return this;
    }

    /**
     * @param condition
     * @类名称:or
     * @类描述:添加or条件
     */
    public SqlBuilder or(Condition condition) {
        this.list.add(Logical.or(condition));
        return this;
    }

    /**
     * @param conditions
     * @类名称:or
     * @类描述:添加or条件
     */
    public SqlBuilder orListAnd(List<Condition> conditions) {
        this.list.add(Logical.or(conditions));
        return this;
    }

    public String getSql(DaoAdapter daoAdapter) {
        return new StringBuffer(this.sql)
                .append(WhereBuilder.generWhereSql(daoAdapter, this.list))
                .append(GroupBuilder.getSubGroupBySql(groupBys))
                .append(HavingBuilder.getSubOrderBySql(having))
                .append(OrderBuilder.getSubOrderBySql(orderBys))
                .toString();
    }
    /**
     * 增加分组
     * @param groupBy
     * @return
     */
    public SqlBuilder addGroupBy(GroupBy groupBy){
        this.groupBys.add(groupBy);
        return this;
    }

    /**
     * 增加排序
     * @param orderBy
     * @return
     */
    public SqlBuilder addOrderBy(OrderBy orderBy) {
        this.orderBys.add(orderBy);
        return this;
    }

    /**
     * 增加having
     * @param having
     * @return
     */
    public SqlBuilder addHaving(Having having) {
        this.having=having;
        return this;
    }


    public Map<String, Object> getValues() {
       return WhereBuilder.getValues(this.list);
    }

    @Override
    @Deprecated
    public String toString() {
        return super.toString();
    }
}
