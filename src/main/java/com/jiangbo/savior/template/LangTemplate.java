package com.jiangbo.savior.template;

import com.jiangbo.savior.adapter.enums.DbTypeEnum;
import com.jiangbo.savior.builder.SqlBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class LangTemplate extends BaseTemplate {

    public LangTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DbTypeEnum dbTypeEnum){
        super.init(namedParameterJdbcTemplate,dbTypeEnum);
    }

    /**
     * 查询基础类型List
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(Class<T> clazz, String sql, Object... params) {
        assertBaseDateType(clazz);
        return executeQuery(()-> getNamedParameterJdbcTemplate().getJdbcTemplate().queryForList(sql, params, clazz));
    }

    /**
     * 查询基础类型
     * @param sqlBuilder
     * @param <T>
     * @return
     */
    public <T> T query(Class<T> clazz, SqlBuilder sqlBuilder) {
        assertBaseDateType(clazz);
        return executeQuery(()->getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.getSql(getDaoAdapter()), sqlBuilder.getValues(), (Class<T>) clazz));
    }

    /**
     * 查询基础类型
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> T query(Class<T> clazz, String sql, Object... params) {
        assertBaseDateType(clazz);
        return executeQuery(()->getNamedParameterJdbcTemplate().getJdbcTemplate().queryForObject(sql, params, clazz));
    }


    /**
     * 批量更新
     * @param sql
     * @param params
     * @return
     */
    public int[] batchUpdate(String sql, List<Object[]> params) {
        return getNamedParameterJdbcTemplate().getJdbcTemplate().batchUpdate(sql, params);
    }

    /**
     * 通过SQL更新
     *
     * @param sql
     * @param params
     * @return
     */
    public int update(String sql, Object... params) {
        if (params == null || params.length < 1) {
            return getNamedParameterJdbcTemplate().getJdbcTemplate().update(sql);
        }
        return getNamedParameterJdbcTemplate().getJdbcTemplate().update(sql, params);
    }

    /**
     * 通过SQL删除数据
     *
     * @param sql
     * @param params
     * @return
     */
    public int delete(String sql, Object... params) {
        if (params == null || params.length < 1) {
            return getNamedParameterJdbcTemplate().getJdbcTemplate().update(sql);
        }
        return getNamedParameterJdbcTemplate().getJdbcTemplate().update(sql, params);
    }

    /**
     * 通过表名删除数据
     *
     * @param tableName
     * @param primaryKey
     * @param val
     * @return
     */
    public int delete(String tableName, String primaryKey, Object val) {
        StringBuffer sf = new StringBuffer("delete from ").append(getDaoAdapter().getSeparator()).append(tableName).append(getDaoAdapter().getSeparator()).append(" where ").append(getDaoAdapter().getSeparator()).append(primaryKey).append(getDaoAdapter().getSeparator()).append("=?");
        return getNamedParameterJdbcTemplate().getJdbcTemplate().update(sf.toString(), val);
    }

    /**
     * 执行SQL语句
     *
     * @param sql
     */
    public void execute(String sql) {
        getNamedParameterJdbcTemplate().getJdbcTemplate().execute(sql);
    }

    /**
     * 能完SQL插入
     *
     * @param sql
     * @param params
     * @return
     */
    public int insert(String sql, Object... params) {
        if (params == null || params.length < 1) {
            return getNamedParameterJdbcTemplate().getJdbcTemplate().update(sql);
        }
        return getNamedParameterJdbcTemplate().getJdbcTemplate().update(sql, params);
    }

    /**
     * 通过SQL批量插入
     *
     * @param sql
     * @param params
     * @return
     */
    public int[] batchInsert(String sql, List<Object[]> params) {
        return getNamedParameterJdbcTemplate().getJdbcTemplate().batchUpdate(sql, params);
    }
}
