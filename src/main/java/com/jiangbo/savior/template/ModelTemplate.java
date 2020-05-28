package com.jiangbo.savior.template;

import com.jiangbo.savior.builder.SqlBuilder;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ModelTemplate extends Template {

    private RecordTemplate recordTemplate;

    public ModelTemplate(RecordTemplate recordTemplate){
        super.init(recordTemplate.getNamedParameterJdbcTemplate(),recordTemplate.getDbTypeEnum());
        this.recordTemplate=recordTemplate;
    }


    /**
     * 通过主键查询对应实体
     *
     * @param tableName
     * @param primaryKey
     * @param value
     * @return
     */
    public <T> T queryPrimaryKey(Function<Record,T> callback, String tableName, String primaryKey, Object value) {
        Record record = this.recordTemplate.queryByPrimaryKey(tableName, primaryKey, value);
        if (record == null) {
            return null;
        }
        return callback.apply(record);
    }

    /**
     * 查询对象
     *
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> T query(Function<Record,T> callback, String sql, Object... params) {
        Record record = this.recordTemplate.query(sql, params);
        if (record == null) {
            return null;
        }
        return callback.apply(record);
    }


    /**
     * 查询List对象
     *
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(Function<Record,T> function, String sql, Object... params) {
        return buildModelList(function,this.recordTemplate.queryList(sql, params));
    }

    private <T> List<T> buildModelList(Function<Record,T> function,List<Record> records){
        if (records == null || records.isEmpty()) {
            return null;
        }
        List<T> rs = new ArrayList<>(records.size());
        for (Record r : records) {
            rs.add(function.apply(r));
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
    public <T> List<T> queryList(Function<Record,T> function, SqlBuilder sqlBuilder) {
        return buildModelList(function,this.recordTemplate.queryList(sqlBuilder));
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
    public <T> Page<T> queryPage(Function<Record,T> function, SqlBuilder sqlBuilder, int pageBeg, int size) {
        Long total = getTotal(sqlBuilder);
        if (total == null || total == 0) {
            return new Page<T>(null, 0l, size, pageBeg);
        }
        return new Page<T>(buildModelList(function, recordTemplate.queryList(getDaoAdapter().getPageSql(sqlBuilder.getSql(getDaoAdapter()), pageBeg, size), sqlBuilder)), total, size, pageBeg);
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
    public <T> Page<T> queryPage(Function<Record,T> function, String sql, int pageBeg, int size, Object... params) {
        Long total = getTotal(sql,params);
        if (total == null || total == 0) {
            return new Page<T>(null, 0l, size, pageBeg);
        }
        return new Page<T>(queryList(function, getDaoAdapter().getPageSql(sql, pageBeg, size), params), total, size, pageBeg);
    }


    /**
     * 插入记录
     *
     * @param tableName
     * @return
     */
    public <T> int insert(String tableName, Function<T,Record> function, T model) {
        assertNull(model);
        return this.recordTemplate.insertSelective(tableName, function.apply(model));
    }

    /**
     * 返回自增加ID值
     *
     * @param tableName
     * @param model
     * @return
     */
    public <T> long insertReturnGeneratedKey(String tableName, Function<T,Record> function,T model) {
        assertNull(model);
        return this.recordTemplate.insertReturnGeneratedKey(tableName, function.apply(model));
    }

    /**
     * 通过表名批量插入
     *
     * @param tableName
     * @return
     */
    public <T> int[] batchInsertByTableName(String tableName, Function<T,Record> function, List<T> models) {
        assertNull(models);
        List<Record> temp = new ArrayList<>(models.size());
        for (T t : models) {
            temp.add(function.apply(t));
        }
        Map<String, Object>[] params = new LinkedCaseInsensitiveMap[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            params[i] = temp.get(i).getData();
        }
        return getNamedParameterJdbcTemplate().batchUpdate(createInsertSql(tableName, temp.get(0), true), params);
    }


    /**
     * 通过表名更新数据
     *
     * @param tableName  表名
     * @param model 记录
     * @param primaryKey 主键名
     * @return
     */
    public <T> int update(String tableName, T model,Function<T,Record> function, String primaryKey) {
        assertNull(model);
        return this.recordTemplate.update(tableName, function.apply(model), primaryKey);
    }

    /**
     * 通过表名更新数据
     *
     * @param tableName  表名
     * @param model 记录
     * @param keys       主键名
     * @return
     */
    public <T> int update(String tableName, T model,Function<T,Record> function, String[] keys) {
        assertNull(model);
        return this.recordTemplate.update(tableName, function.apply(model), keys);
    }

    /**
     * 保存数据
     *
     * @param tableName  表名
     * @param model 记录
     * @param primaryKey  主键名
     * @return
     */
    public <T> int save(String tableName, T model,Function<T,Record> function,  String primaryKey) {
        assertNull(model);
        return this.recordTemplate.save(tableName, function.apply(model),primaryKey);
    }

    /**
     * 如果tableModle中的属性为空则不做更新
     *
     * @param tableName
     * @param model
     * @param primaryKey
     * @return
     */
    public <T> int updateSelective(String tableName, T model,Function<T,Record> function, String primaryKey) {
        assertNull(model);
        return this.recordTemplate.updateSelective(tableName,function.apply(model), primaryKey);

    }

    /**
     * 如果tableModle中的属性为空则不做更新
     *
     * @param tableName
     * @param model
     * @param keys
     * @return
     */
    public <T> int updateSelective(String tableName, T model,Function<T,Record> function, String[] keys) {
        assertNull(model);
        return this.recordTemplate.updateSelective(tableName,function.apply(model), keys);
    }

    /**
     * 通过表名批量更新
     *
     * @param tableName
     * @param models
     * @param primaryKey
     * @return
     */
    public <T> int[] batchUpdate(String tableName, List<T> models,Function<T,Record> function, String primaryKey) {
        assertNull(models);
        List<Record> temp = new ArrayList<>(models.size());
        for (T t : models) {
            temp.add(function.apply(t));
        }
        return this.recordTemplate.batchUpdate(tableName, temp, primaryKey);
    }

}
