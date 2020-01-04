package com.jiangbo.savior.mapper;

import com.jiangbo.savior.SaviorDao;
import com.jiangbo.savior.builder.SqlBuilder;
import com.jiangbo.savior.builder.order.OrderBy;
import com.jiangbo.savior.builder.where.Condition;
import com.jiangbo.savior.model.BaseTable;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Supplier;

public abstract class BaseMapper<T extends BaseTable, ID> {

    @Autowired(required = false)
    private SaviorDao dao;

    private final String BASE_QUERY_SQL = "select * from ";

    protected abstract String tableName();

    protected abstract Supplier<T> getTable();

    protected abstract String primaryKey();

    public T queryTable(ID id) {
        return dao.tableTemplate.queryByPrimaryKey(getTable(), tableName(), primaryKey(), id);
    }

    public List<T> queryTable(Iterable<ID> id) {
        SqlBuilder sqb = SqlBuilder.getSelect(BASE_QUERY_SQL + tableName());
        sqb.and(Condition.in(primaryKey(), id));
        return dao.tableTemplate.queryList(getTable(), sqb);
    }

    public List<T> queryAll() {
        return dao.tableTemplate.queryList(getTable(), BASE_QUERY_SQL + tableName());
    }

    public List<T> queryAll(final OrderBy[] orders) {
        AssertUtils.isNull(orders, "排序字段不能为空!");
        SqlBuilder sqb = SqlBuilder.getSelect(BASE_QUERY_SQL + tableName());
        for (OrderBy order : orders) {
            sqb.addOrderBy(order);
        }
        return dao.tableTemplate.queryList(getTable(), sqb);
    }

    public Page<T> queryPage(int pageBeg, int size){
        return dao.tableTemplate.queryPage(getTable(),BASE_QUERY_SQL+getTable(),pageBeg,size);
    }

    public <S extends T> int saveTable(S table){
        return dao.tableTemplate.save(tableName(),table,primaryKey());
    }

    public <S extends T> int insertTable(S table){
        return dao.tableTemplate.insert(tableName(),table);
    }

    public <S extends T> int updateTable(S table){
        return dao.tableTemplate.update(tableName(),table,primaryKey());
    }

    public <S extends T> int updateSelectiveTable(S table,String primaryKey){
        return dao.tableTemplate.updateSelective(tableName(),table,primaryKey);
    }
}
