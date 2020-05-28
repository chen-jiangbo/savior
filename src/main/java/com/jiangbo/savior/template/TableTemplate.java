package com.jiangbo.savior.template;

import com.jiangbo.savior.builder.SqlBuilder;
import com.jiangbo.savior.model.BaseTable;
import com.jiangbo.savior.model.Context;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.utils.StringUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TableTemplate extends Template {

    private RecordTemplate recordTemplate;

    public TableTemplate(RecordTemplate recordTemplate){
        super.init(recordTemplate.getNamedParameterJdbcTemplate(),recordTemplate.getDbTypeEnum());
        this.recordTemplate=recordTemplate;
    }
    /**
     * 通过主键查询对应实体
     * @param tableName
     * @param primaryKey
     * @param value
     * @return
     */
    public <T extends BaseTable> T queryByPrimaryKey(final Supplier<T> supplier, String tableName, String primaryKey, Object value) {
        return queryByPrimaryKey(supplier, null, tableName, primaryKey, value);
    }

    /**
     * 通过主键查询对应实体
     * @param tableName
     * @param primaryKey
     * @param value
     * @return
     */
    public <T extends BaseTable> T queryByPrimaryKey(final Supplier<T> supplier, Function<Context,T> function, String tableName, String primaryKey, Object value) {
        Record record = this.recordTemplate.queryByPrimaryKey(tableName, primaryKey, value);
        T t = supplier.get();
        if (function != null) {
            Context context=new Context();
            function.apply(context);
            t.initContext(context);
        }
        return record == null ? null : record.convertTable(t);
    }

    /**
     * 查询对象
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T extends BaseTable> T query(final Supplier<T> supplier, Function<Context,T> function, String sql, Object... params) {
        Record record = this.recordTemplate.query(sql, params);
        T t = supplier.get();
        if (function != null) {
            Context context = new Context();
            function.apply(context);
            t.initContext(context);
        }
        return record == null ? null : record.convertTable(t);
    }

    /**
     * 查询对象
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T extends BaseTable> T query(final Supplier<T> supplier, String sql, Object... params) {
        return query(supplier, null, sql, params);
    }


    /**
     * 查询List对象
     *
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T extends BaseTable> List<T> queryList(final Supplier<T> supplier, Function<Context,T> function, String sql, Object... params) {
        return buildListTableModel(this.recordTemplate.queryList(sql, params), supplier, function);
    }

    /**
     * 装配列表TableModle
     *
     * @param records
     * @param supplier
     * @param function
     * @param <T>
     * @return
     */
    private <T extends BaseTable> List<T> buildListTableModel(List<Record> records, final Supplier<T> supplier, Function<Context,T> function) {
        if (records == null || records.isEmpty()) {
            return null;
        }
        List<T> rs = new ArrayList<T>(records.size());
        for (Record r : records) {
            T t = supplier.get();
            if (function != null) {
                Context context = new Context();
                function.apply(context);
                t.initContext(context);
            }
            rs.add(r.convertTable(t));
        }
        return rs;
    }

    /**
     * 查询List对象
     *
     * @param sqlBuilder
     * @param <T>
     * @return
     */
    private <T extends BaseTable> List<T> queryList(final Supplier<T> supplier, String sql, SqlBuilder sqlBuilder, Function<Context,T> function) {
        return buildListTableModel(this.recordTemplate.queryList(sql, sqlBuilder), supplier, function);
    }


    /**
     * 查询List对象
     *
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T extends BaseTable> List<T> queryList(final Supplier<T> supplier, String sql, Object... params) {
        return queryList(supplier, null, sql, params);
    }
    /**
     * 查询分页对象
     *
     * @param sql
     * @param pageBeg
     * @param size
     * @param params
     * @param <T>
     * @return
     */
    public <T extends BaseTable> Page<T> queryPage(final Supplier<T> supplier, Function<Context,T> function, String sql, int pageBeg, int size, Object... params) {
        Long total = getTotal(sql,params);
        if (total == null || total == 0) {
            return new Page<T>(null, 0l, size, pageBeg);
        }
        return new Page<T>(queryList(supplier, function, getDaoAdapter().getPageSql(sql, pageBeg, size), params), total, size, pageBeg);
    }

    /**
     * 查询分页对象
     *
     * @param sql
     * @param pageBeg
     * @param size
     * @param params
     * @param <T>
     * @return
     */
    public <T extends BaseTable> Page<T> queryPage(final Supplier<T> supplier, String sql, int pageBeg, int size, Object... params) {
        return queryPage(supplier, null, sql, pageBeg, size, params);
    }

    /**
     * 查询对象
     *
     * @param sqlBuilder
     * @param <T>
     * @return
     */
    public <T extends BaseTable> T query(final Supplier<T> supplier, SqlBuilder sqlBuilder) {
        Record record = this.recordTemplate.query(sqlBuilder);
        return record == null ? null : record.convertTable(supplier.get());
    }

    /**
     * 查询List对象
     *
     * @param sqlBuilder
     * @param <T>
     * @return
     */
    public <T extends BaseTable> List<T> queryList(final Supplier<T> supplier, SqlBuilder sqlBuilder) {
        return queryList(supplier, sqlBuilder.getSql(getDaoAdapter()), sqlBuilder, null);
    }

    /**
     * 查询List对象
     *
     * @param sqlBuilder
     * @param <T>
     * @return
     */
    public <T extends BaseTable> List<T> queryList(final Supplier<T> supplier, SqlBuilder sqlBuilder, Function<Context,T> function) {
        return queryList(supplier, sqlBuilder.getSql(getDaoAdapter()), sqlBuilder, function);
    }

    /**
     * 查询List对象
     *
     * @param sqlBuilder
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(Class<T> clazz, SqlBuilder sqlBuilder) {
        assertBaseDateType(clazz);
        return executeQuery(()->getNamedParameterJdbcTemplate().queryForList(sqlBuilder.getSql(getDaoAdapter()), sqlBuilder.getValues(), clazz));
    }

    /**
     * 查询分页对象
     *
     * @param sqlBuilder
     * @param pageBeg
     * @param size
     * @param <T>
     * @return
     */
    public <T extends BaseTable> Page<T> queryPage(final Supplier<T> supplier, SqlBuilder sqlBuilder, Function<Context,T> function, int pageBeg, int size) {
        Long total = getTotal(sqlBuilder);
        if (total == null || total == 0) {
            return new Page<T>(null, 0l, size, pageBeg);
        }
        return new Page<T>(queryList(supplier, getDaoAdapter().getPageSql(sqlBuilder.getSql(getDaoAdapter()), pageBeg, size), sqlBuilder, function), total, size, pageBeg);
    }

    /**
     * 查询分页对象
     *
     * @param sqlBuilder
     * @param pageBeg
     * @param size
     * @param <T>
     * @return
     */
    public <T extends BaseTable> Page<T> queryPage(final Supplier<T> supplier, SqlBuilder sqlBuilder, int pageBeg, int size) {
        Long total = getTotal(sqlBuilder);
        if (total == null || total == 0) {
            return new Page<T>(null, 0l, size, pageBeg);
        }
        return new Page<T>(queryList(supplier, getDaoAdapter().getPageSql(sqlBuilder.getSql(getDaoAdapter()), pageBeg, size), sqlBuilder, null), total, size, pageBeg);
    }


    /**
     * 插入记录
     *
     * @param tableName
     * @return
     */
    public <T extends BaseTable> int insert(String tableName, T tableModle) {
        assertNull(tableModle);
        return this.recordTemplate.insertSelective(tableName, tableModle.reversal());
    }

    /**
     * 返回自增加ID值
     *
     * @param tableName
     * @param tableModle
     * @return
     */
    public <T extends BaseTable> long insertReturnGeneratedKey(String tableName, T tableModle) {
        assertNull(tableModle);
        return this.recordTemplate.insertReturnGeneratedKey(tableName, tableModle.reversal());
    }

    /**
     * 通过表名批量插入
     *
     * @param tableName
     * @return
     */
    public <T extends BaseTable> int[] batchInsertByTableName(String tableName, List<T> tableModles) {
        assertNull(tableModles);
        List<Record> temp = new ArrayList<>(tableModles.size());
        for (T t : tableModles) {
            temp.add(((BaseTable) t).reversal());
        }
        Map<String, Object>[] params = new LinkedCaseInsensitiveMap[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            params[i] = temp.get(i).getData();
        }
        return getNamedParameterJdbcTemplate().batchUpdate(createInsertSql(tableName, temp.get(0), true), params);
    }


    /**
     * 通过表名更新数据
     * @param tableName  表名
     * @param tableModle 记录
     * @param primaryKey 主键名
     * @return
     */
    public <T extends BaseTable> int update(String tableName, T tableModle, String primaryKey) {
        assertNull(tableModle);
        return this.recordTemplate.update(tableName, tableModle.reversal(), primaryKey);
    }

    /**
     * 通过表名更新数据
     * @param tableName  表名
     * @param tableModle 记录
     * @param primaryKey 主键名
     * @param ignoreCloumns 更新时需要忽略的列名
     * @return
     */
    public <T extends BaseTable> int update(String tableName, T tableModle, String primaryKey, String[] ignoreCloumns) {
        assertNull(tableModle);
        Record record = tableModle.reversal();
        if (ignoreCloumns != null && ignoreCloumns.length > 0) {
            Stream.of(ignoreCloumns).filter((o) -> StringUtils.isNotBlank(o)).forEach((o)->record.remove(o));
        }
        return this.recordTemplate.update(tableName, record, primaryKey);
    }

    /**
     * 通过表名更新指定数据
     * @param tableName  表名
     * @param tableModle 记录
     * @param primaryKey 主键名
     * @param cloumns 要求更新的列名
     * @return
     */
    public <T extends BaseTable> int updateSpecify(String tableName, T tableModle, String primaryKey, String[] cloumns) {
        assertNull(tableModle);
        assertNull(cloumns);
        Record record = tableModle.reversal();
        Record result = new Record();
        result.setColumn(primaryKey, record.get(primaryKey));
        Stream.of(cloumns).filter((o) -> StringUtils.isNotBlank(o)).forEach((o) -> result.setColumn(o, record.get(o)));
        return this.recordTemplate.update(tableName, result, primaryKey);
    }



    /**
     * 保存数据(如果主键存在就更新,不存在就插入)
     * @param tableName  表名
     * @param tableModle 记录
     * @param primaryKey 主键名
     * @return
     */
    public <T extends BaseTable> int save(String tableName, T tableModle, String primaryKey) {
        assertNull(tableModle);
        return this.recordTemplate.save(tableName, tableModle.reversal(), primaryKey);
    }

    /**
     * 通过表名更新数据
     *
     * @param tableName  表名
     * @param tableModle 记录
     * @param keys       主键名
     * @return
     */
    public <T extends BaseTable> int update(String tableName, T tableModle, String[] keys) {
        assertNull(tableModle);
        return this.recordTemplate.update(tableName, tableModle.reversal(), keys);
    }

    /**
     * 如果tableModle中的属性为空则不做更新
     *
     * @param tableName
     * @param tableModle
     * @param primaryKey
     * @return
     */
    public <T extends BaseTable> int updateSelective(String tableName, T tableModle, String primaryKey) {
        assertNull(tableModle);
        return this.recordTemplate.updateSelective(tableName, tableModle.reversal(), primaryKey);
    }

    /**
     * 如果tableModle中的属性为空则不做更新
     *
     * @param tableName
     * @param tableModle
     * @param keys
     * @return
     */
    public <T extends BaseTable> int updateSelective(String tableName, T tableModle, String[] keys) {
        assertNull(tableModle);
        return this.recordTemplate.updateSelective(tableName, tableModle.reversal(), keys);
    }

    /**
     * 通过表名批量更新
     * @param tableName
     * @param tableModles
     * @param primaryKey
     * @param ignoreCloumns
     * @return
     */
    public <T extends BaseTable> int[] batchUpdate(String tableName, List<T> tableModles, String primaryKey, String[] ignoreCloumns) {
        assertNull(tableModles);
        List<Record> temp = new ArrayList<>(tableModles.size());
        for (T t : tableModles) {
            Record r = t.reversal();
            if (ignoreCloumns != null && ignoreCloumns.length > 0) {
                Stream.of(ignoreCloumns).filter((o) -> StringUtils.isNotBlank(o)).forEach((o) -> {
                    r.remove(o);
                });
            }
            temp.add(r);
        }
        return this.recordTemplate.batchUpdate(tableName, temp, primaryKey);
    }

    /**
     * 通过表名批量更新指定字段
     * @param tableName
     * @param tableModles
     * @param primaryKey
     * @param cloumns
     * @return
     */
    public <T extends BaseTable> int[] batchUpdateSpecify(String tableName, List<T> tableModles, String primaryKey, String[] cloumns) {
        assertNull(tableModles);
        assertNull(cloumns);
        List<Record> temp = new ArrayList<>(tableModles.size());
        for (T t : tableModles) {
            Record r = t.reversal();
            Record tr = new Record();
            //确保主键一定是有值的
            tr.setColumn(primaryKey,r.get(primaryKey));
            for (String c : cloumns) {
                tr.setColumn(c, r.get(c));
            }
            temp.add(tr);
        }
        return this.recordTemplate.batchUpdate(tableName, temp, primaryKey);
    }

    /**
     * 通过表名批量更新
     * @param tableName
     * @param tableModles
     * @param primaryKey
     * @return
     */
    public <T extends BaseTable> int[] batchUpdate(String tableName, List<T> tableModles, String primaryKey) {
        assertNull(tableModles);
        List<Record> temp = new ArrayList<>(tableModles.size());
        for (T t : tableModles) {
            temp.add(t.reversal());
        }
        return this.recordTemplate.batchUpdate(tableName, temp, primaryKey);
    }

    /**
     * 通过表名批量删除
     * @param tableName
     * @param tableModles
     * @param primaryKey
     * @return
     */
    public <T extends BaseTable> int[] batchDelete(String tableName, List<T> tableModles, String primaryKey) {
        assertNull(tableModles);
        List<Record> temp = new ArrayList<>(tableModles.size());
        for (T t : tableModles) {
            temp.add(t.reversal());
        }
        return this.recordTemplate.batchDelete(tableName, temp, primaryKey);
    }

    /**
     * 删除表数据
     * @param tableName
     * @param tableModles
     * @param primaryKey
     * @param <T>
     * @return
     */
    public <T extends BaseTable> int delete(String tableName, T tableModles, String primaryKey){
        return this.recordTemplate.delete(tableName, tableModles.reversal(), primaryKey);
    }


}
