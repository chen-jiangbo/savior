package com.jiangbo.savior.template;

import com.jiangbo.savior.adapter.DaoAdapter;
import com.jiangbo.savior.adapter.enums.DbTypeEnum;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.exception.InavlidLangTypeException;
import com.jiangbo.savior.exception.NullDataException;
import com.jiangbo.savior.builder.SqlBuilder;
import com.jiangbo.savior.callback.IEmptyResult;
import com.jiangbo.savior.utils.ObjectUtils;
import com.jiangbo.savior.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseTemplate {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private DbTypeEnum dbTypeEnum;

    protected DbTypeEnum getDbTypeEnum(){
        return this.dbTypeEnum;
    }

    protected DaoAdapter getDaoAdapter(){
        return dbTypeEnum.getDaoAdapter();
    }

    protected void init(NamedParameterJdbcTemplate namedParameterJdbcTemplate,DbTypeEnum dbTypeEnum){
        this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
        this.dbTypeEnum=dbTypeEnum;
    }

    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    protected <T> boolean isBaseDateType(Class<T> clazz) {
        if (clazz.isAssignableFrom(Integer.class)
                || clazz.isAssignableFrom(Float.class)
                || clazz.isAssignableFrom(Double.class)
                || clazz.isAssignableFrom(String.class)
                || clazz.isAssignableFrom(Long.class)
                || clazz.isAssignableFrom(BigDecimal.class)
                || clazz.isAssignableFrom(Date.class)) {
            return true;
        }
        return false;
    }


    protected <T> void assertBaseDateType(Class<T> clazz){
        if (!isBaseDateType(clazz)) {
            throw new InavlidLangTypeException();
        }
    }


    protected void assertNull(Object object){
        if(ObjectUtils.isEmpty(object)){
            throw new NullDataException();
        }
    }

    protected <T> T executeQuery(IEmptyResult<T> result) {
        try {
            return result.execute();
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 创建更新语句的SQL
     *
     * @param tableName    表名
     * @param record       要插入的数据
     * @param primaryKey   主键
     * @param isUpdateNull 是否要更新属性为空的值
     * @return
     */
    protected String createUpdateSql(String tableName, Record record, String primaryKey, boolean isUpdateNull) {
        StringBuffer sf = new StringBuffer("update ").append(getDaoAdapter().getSeparator()).append(tableName).append(getDaoAdapter().getSeparator()).append(" set ");
        for (String k : record.getData().keySet()) {
            if (StringUtils.equals(k, primaryKey)) {
                continue;
            }
            if (!isUpdateNull && record.get(k) == null) {
                continue;
            }
            sf.append(getDaoAdapter().getSeparator()).append(k).append(getDaoAdapter().getSeparator()).append("=:").append(k).append(" ,");
        }
        String sql = sf.substring(0, sf.length() - 1);
        sql = sql + " where " +getDaoAdapter().getSeparator()+primaryKey + getDaoAdapter().getSeparator()+"=:" + primaryKey;
        return sql;
    }


    /**
     * 创建更新语句的SQL
     *
     * @param tableName    表名
     * @param record       要插入的数据
     * @param keys         主键
     * @param isUpdateNull 是否要更新属性为空的值
     * @return
     */
    protected String createUpdateSql(String tableName, Record record, String[] keys, boolean isUpdateNull) {
        Set<String> rs = new HashSet<String>(keys.length);
        StringBuffer endWhereSql = new StringBuffer(" where 1=1 ");
        for (String key : keys) {
            endWhereSql.append(" and ").append(getDaoAdapter().getSeparator()).append(key).append(getDaoAdapter().getSeparator()).append("=:").append(key);
            rs.add(key);
        }
        StringBuffer sf = new StringBuffer("update ").append(getDaoAdapter().getSeparator()).append(tableName).append(getDaoAdapter().getSeparator()).append(" set ");
        for (String k : record.getData().keySet()) {
            if (rs.contains(k)) {
                continue;
            }
            if (!isUpdateNull && record.get(k) == null) {
                continue;
            }
            sf.append(getDaoAdapter().getSeparator()).append(k).append(getDaoAdapter().getSeparator()).append("=:").append(k).append(" ,");
        }
        String sql = sf.substring(0, sf.length() - 1);
        sql = sql + endWhereSql.toString();
        return sql;
    }


    /**
     * 创建查询的SQL
     *
     * @param tableName
     * @param record
     * @param isInsertNull
     * @return
     */
    protected String createInsertSql(String tableName, Record record, boolean isInsertNull) {
        StringBuffer sfPre = new StringBuffer("insert into ").append(getDaoAdapter().getSeparator()).append(tableName).append(getDaoAdapter().getSeparator()).append(" (");
        StringBuffer sfVal = new StringBuffer(" ) values ( ");
        for (String key : record.getData().keySet()) {
            if (!isInsertNull && record.get(key) == null) {
                continue;
            }
            sfPre.append(getDaoAdapter().getSeparator()).append(key).append(getDaoAdapter().getSeparator()).append(",");
            sfVal.append(":").append(key).append(",");
        }
        return sfPre.substring(0, sfPre.length() - 1) + sfVal.substring(0, sfVal.length() - 1) + " ) ";
    }

    /**
     * 获取sql查询结果的总条数
     * @param sql
     * @param params
     * @return
     */
    protected Long getTotal(String sql, Object... params) {
        StringBuffer sqb = new StringBuffer();
        sql = sql.toLowerCase();
        if (sql.lastIndexOf("order by") > 0) {
            sql = sql.substring(0, sql.indexOf("order by"));
        }
        sqb.append("select count(1) from (" + sql + ") tmp");
        return executeQuery(() ->getNamedParameterJdbcTemplate().getJdbcTemplate().queryForObject(sqb.toString(), params, Long.class));
    }

    /**
     * 获取sql查询结果的总条数
     * @return
     */
    protected Long getTotal(SqlBuilder sqlBuilder) {
        StringBuffer sqb = new StringBuffer();
        String sql = sqlBuilder.getSql(getDaoAdapter()).toLowerCase();
        if (sql.lastIndexOf("order by") > 0) {
            sql = sql.substring(0, sql.indexOf("order by"));
        }
        sqb.append("select count(1) from (" + sql + ") tmp");
        return executeQuery(() ->getNamedParameterJdbcTemplate().queryForObject(sqb.toString(), sqlBuilder.getValues(), Long.class));
    }

}
