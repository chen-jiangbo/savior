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
    protected SaviorDao dao;

    private MapperTableInfo<T> mapperTableInfo;

    public BaseMapper() {
        this.mapperTableInfo=mapperTableInfo();
    }

    public BaseMapper(SaviorDao dao) {
        this.dao=dao;
        this.mapperTableInfo=mapperTableInfo();
    }

    private final String BASE_QUERY_SQL = "select * from ";

    protected abstract MapperTableInfo<T> mapperTableInfo();


    public T queryTable(ID id) {
        return dao.tableTemplate.queryByPrimaryKey(mapperTableInfo.getSupplier(),mapperTableInfo.getTableName(), mapperTableInfo.getPrimaryKey(), id);
    }

    public List<T> queryTable(Iterable<ID> id) {
        SqlBuilder sqb = SqlBuilder.getSelect(BASE_QUERY_SQL + mapperTableInfo.getTableName());
        sqb.and(Condition.in(mapperTableInfo.getPrimaryKey(), id));
        return dao.tableTemplate.queryList(mapperTableInfo.getSupplier(), sqb);
    }

    public List<T> queryAll() {
        return dao.tableTemplate.queryList(mapperTableInfo.getSupplier(), BASE_QUERY_SQL + mapperTableInfo.getTableName());
    }

    public List<T> queryAll(final OrderBy[] orders) {
        AssertUtils.isNull(orders, "排序字段不能为空!");
        SqlBuilder sqb = SqlBuilder.getSelect(BASE_QUERY_SQL + mapperTableInfo.getTableName());
        for (OrderBy order : orders) {
            sqb.addOrderBy(order);
        }
        return dao.tableTemplate.queryList(mapperTableInfo.getSupplier(), sqb);
    }

    public Page<T> queryPage(int pageBeg, int size){
        return dao.tableTemplate.queryPage(mapperTableInfo.getSupplier(),BASE_QUERY_SQL+mapperTableInfo.getTableName(),pageBeg,size);
    }

    public <S extends T> int saveTable(S table){
        return dao.tableTemplate.save(mapperTableInfo.getTableName(),table,mapperTableInfo.getPrimaryKey());
    }

    public <S extends T> int insertTable(S table){
        return dao.tableTemplate.insert(mapperTableInfo.getTableName(),table);
    }

    public <S extends T> int updateTable(S table){
        return dao.tableTemplate.update(mapperTableInfo.getTableName(),table,mapperTableInfo.getPrimaryKey());
    }

    public <S extends T> int updateSelectiveTable(S table,String primaryKey){
        return dao.tableTemplate.updateSelective(mapperTableInfo.getTableName(),table,primaryKey);
    }
}
