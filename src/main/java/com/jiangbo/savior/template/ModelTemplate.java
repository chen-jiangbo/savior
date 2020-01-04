package com.jiangbo.savior.template;

import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.builder.SqlBuilder;
import com.jiangbo.savior.callback.IModelQueryCallback;
import com.jiangbo.savior.callback.IModelUpdateCallBack;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelTemplate extends BaseTemplate {

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
    public <T> T queryPrimaryKey(IModelQueryCallback<T> callback, String tableName, String primaryKey, Object value) {
        Record record = this.recordTemplate.queryByPrimaryKey(tableName, primaryKey, value);
        if (record == null) {
            return null;
        }
        return callback.buildModel(record);
    }

    /**
     * 查询对象
     *
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> T query(IModelQueryCallback<T> callback, String sql, Object... params) {
        Record record = this.recordTemplate.query(sql, params);
        if (record == null) {
            return null;
        }
        return callback.buildModel(record);
    }


    /**
     * 查询List对象
     *
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(IModelQueryCallback<T> queryCallback, String sql, Object... params) {
        return buildModelList(queryCallback,this.recordTemplate.queryList(sql, params));
    }

    private <T> List<T> buildModelList(IModelQueryCallback<T> queryCallback,List<Record> records){
        if (records == null || records.isEmpty()) {
            return null;
        }
        List<T> rs = new ArrayList<>(records.size());
        for (Record r : records) {
            rs.add(queryCallback.buildModel(r));
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
    public <T> List<T> queryList(IModelQueryCallback<T> queryCallback, SqlBuilder sqlBuilder) {
        return buildModelList(queryCallback,this.recordTemplate.queryList(sqlBuilder));
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
    public <T> Page<T> queryPage(IModelQueryCallback<T> queryCallback, SqlBuilder sqlBuilder, int pageBeg, int size) {
        Long total = getTotal(sqlBuilder);
        if (total == null || total == 0) {
            return new Page<T>(null, 0l, size, pageBeg);
        }
        return new Page<T>(queryList(queryCallback, getDaoAdapter().getPageSql(sqlBuilder.getSql(getDaoAdapter()), pageBeg, size), sqlBuilder), total, size, pageBeg);
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
    public <T> Page<T> queryPage(IModelQueryCallback<T> queryCallback, String sql, int pageBeg, int size, Object... params) {
        Long total = getTotal(sql,params);
        if (total == null || total == 0) {
            return new Page<T>(null, 0l, size, pageBeg);
        }
        return new Page<T>(queryList(queryCallback, getDaoAdapter().getPageSql(sql, pageBeg, size), params), total, size, pageBeg);
    }


    /**
     * 插入记录
     *
     * @param tableName
     * @return
     */
    public <T> int insert(String tableName, IModelUpdateCallBack<T> updateCallBack, T model) {
        assertNull(model);
        return this.recordTemplate.insertSelective(tableName, updateCallBack.buildRecord(new Record(),model));
    }

    /**
     * 返回自增加ID值
     *
     * @param tableName
     * @param model
     * @return
     */
    public <T> long insertReturnGeneratedKey(String tableName, IModelUpdateCallBack<T> updateCallBack,T model) {
        assertNull(model);
        return this.recordTemplate.insertReturnGeneratedKey(tableName, updateCallBack.buildRecord(new Record(),model));
    }

    /**
     * 通过表名批量插入
     *
     * @param tableName
     * @return
     */
    public <T> int[] batchInsertByTableName(String tableName, IModelUpdateCallBack<T> updateCallBack, List<T> models) {
        assertNull(models);
        List<Record> temp = new ArrayList<>(models.size());
        for (T t : models) {
            temp.add(updateCallBack.buildRecord(new Record(),t));
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
    public <T> int update(String tableName, T model,IModelUpdateCallBack<T> updateCallBack, String primaryKey) {
        assertNull(model);
        return this.recordTemplate.update(tableName, updateCallBack.buildRecord(new Record(),model), primaryKey);
    }

    /**
     * 通过表名更新数据
     *
     * @param tableName  表名
     * @param model 记录
     * @param keys       主键名
     * @return
     */
    public <T> int update(String tableName, T model,IModelUpdateCallBack<T> updateCallBack, String[] keys) {
        assertNull(model);
        return this.recordTemplate.update(tableName, updateCallBack.buildRecord(new Record(),model), keys);
    }

    /**
     * 保存数据
     *
     * @param tableName  表名
     * @param model 记录
     * @param primaryKey  主键名
     * @return
     */
    public <T> int save(String tableName, T model,IModelUpdateCallBack<T> updateCallBack,  String primaryKey) {
        assertNull(model);
        return this.recordTemplate.save(tableName, updateCallBack.buildRecord(new Record(),model),primaryKey);
    }

    /**
     * 如果tableModle中的属性为空则不做更新
     *
     * @param tableName
     * @param model
     * @param primaryKey
     * @return
     */
    public <T> int updateSelective(String tableName, T model,IModelUpdateCallBack<T> updateCallBack, String primaryKey) {
        assertNull(model);
        return this.recordTemplate.updateSelective(tableName,updateCallBack.buildRecord(new Record(),model), primaryKey);

    }

    /**
     * 如果tableModle中的属性为空则不做更新
     *
     * @param tableName
     * @param model
     * @param keys
     * @return
     */
    public <T> int updateSelective(String tableName, T model,IModelUpdateCallBack<T> updateCallBack, String[] keys) {
        assertNull(model);
        return this.recordTemplate.updateSelective(tableName,updateCallBack.buildRecord(new Record(),model), keys);
    }

    /**
     * 通过表名批量更新
     *
     * @param tableName
     * @param models
     * @param primaryKey
     * @return
     */
    public <T> int[] batchUpdate(String tableName, List<T> models,IModelUpdateCallBack<T> updateCallBack, String primaryKey) {
        assertNull(models);
        List<Record> temp = new ArrayList<>(models.size());
        for (T t : models) {
            temp.add(updateCallBack.buildRecord(new Record(),t));
        }
        return this.recordTemplate.batchUpdate(tableName, temp, primaryKey);
    }

}
