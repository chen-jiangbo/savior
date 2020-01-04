package com.jiangbo.savior.template;

import com.jiangbo.savior.exception.NullDataException;
import com.jiangbo.savior.exception.ServiceException;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.builder.SqlBuilder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordTemplate extends BaseTemplate {

    private LangTemplate langTemplate;

    public RecordTemplate(LangTemplate langTemplate){
        super.init(langTemplate.getNamedParameterJdbcTemplate(),langTemplate.getDbTypeEnum());
        this.langTemplate=langTemplate;
    }
    
    /**
     * 查询数据
     *
     * @param sql
     * @param params
     * @return
     */
    public Record query(String sql, Object... params) {
        Map<String, Object> map = executeQuery(() -> getNamedParameterJdbcTemplate().getJdbcTemplate().queryForMap(sql, params));
        return map == null ? null : new Record(map);
    }

    /**
     * 通过主键查询记录
     *
     * @param tableName
     * @param primaryKey
     * @param value
     * @return
     */
    public Record queryByPrimaryKey(String tableName, String primaryKey, Object value) {
        Map<String, Object> map = executeQuery(() -> {
            StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(" where ").append(primaryKey).append("=?");
            return getNamedParameterJdbcTemplate().getJdbcTemplate().queryForMap(sql.toString(), value);
        });
        return map == null ? null : new Record(map);
    }

    /**
     * 查询List记录
     *
     * @param sql
     * @param params
     * @return
     */
    public List<Record> queryList(String sql, Object... params) {
        return convertRecords(executeQuery(() -> getNamedParameterJdbcTemplate().getJdbcTemplate().queryForList(sql, params)));
    }

    /**
     * 转换listMap为listRecord
     * @param maps
     * @return
     */
    protected List<Record> convertRecords(List<Map<String, Object>> maps){
        if (maps == null || maps.isEmpty()) {
            return null;
        }
        List<Record> rs = new ArrayList<Record>(maps.size());
        for (Map<String, Object> m : maps) {
            rs.add(new Record(m));
        }
        return rs;
    }

    /**
     * 查询分页记录
     *
     * @param sql
     * @param pageBeg
     * @param size
     * @param params
     * @return
     */
    public Page<Record> queryPage(String sql, int pageBeg, int size, Object... params) {
        Long total = executeQuery(() -> getNamedParameterJdbcTemplate().getJdbcTemplate().queryForObject("select count(1) from " + (sql.toLowerCase().split("from", 2))[1], params, Long.class));
        if (total == null || total == 0) {
            return new Page<Record>(null, 0l, size, pageBeg);
        }
        return new Page<Record>(queryList(getDaoAdapter().getPageSql(sql, pageBeg, size), params), total, size, pageBeg);
    }


    public int insert(String tableName, Record record) {
        if (record == null || record.getData() == null) {
            throw new NullDataException("要插入的数据为空!");
        }
        return getNamedParameterJdbcTemplate().update(createInsertSql(tableName, record, true), record.getData());
    }

    /**
     * 返回自增加ID值
     *
     * @param tableName
     * @param record
     * @return
     */
    public long insertReturnGeneratedKey(String tableName, Record record) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(createInsertSql(tableName, record, true), new MapSqlParameterSource(record.getData()), keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * 插入记录
     *
     * @param tableName
     * @param record
     * @return
     */
    public int insertSelective(String tableName, Record record) {
        if (record == null || record.getData() == null) {
            throw new NullDataException("要插入的数据为空!");
        }
        return getNamedParameterJdbcTemplate().update(createInsertSql(tableName, record, false), record.getData());
    }

    /**
     * 通过表名批量插入
     *
     * @param tableName
     * @return
     */
    public int[] batchInsertByTableName(String tableName, List<Record> records) {
        assertNull(records);
        Map<String, Object>[] params = new LinkedCaseInsensitiveMap[records.size()];
        for (int i = 0; i < records.size(); i++) {
            params[i] = records.get(i).getData();
        }
        return getNamedParameterJdbcTemplate().batchUpdate(createInsertSql(tableName, records.get(0), true), params);
    }

    /**
     * 通过表名更新数据
     * @param tableName  表名
     * @param record     记录
     * @param primaryKey 主键名
     * @return
     */
    public int update(String tableName, Record record, String primaryKey) {
        return getNamedParameterJdbcTemplate().update(createUpdateSql(tableName, record, primaryKey, true), record.getData());
    }

    /**
     * 保存数据(如果主键存在就更新,不存在就插入)
     * @param tableName  表名
     * @param record     记录
     * @param primaryKey 主键名
     * @return
     */
    public int save(String tableName, Record record, String primaryKey) {
        if(record.get(primaryKey)!=null){
            return update(tableName,record,primaryKey);
        }
        return insert(tableName,record);
    }

    /**
     * 通过表名更新数据
     *
     * @param tableName 表名
     * @param record    记录
     * @param keys      主键名
     * @return
     */
    public int update(String tableName, Record record, String[] keys) {
        return getNamedParameterJdbcTemplate().update(createUpdateSql(tableName, record, keys, true), record.getData());
    }

    /**
     * 如果record中的属性为空则不做更新
     *
     * @param tableName
     * @param record
     * @param primaryKey
     * @return
     */
    public int updateSelective(String tableName, Record record, String primaryKey) {
        return getNamedParameterJdbcTemplate().update(createUpdateSql(tableName, record, primaryKey, false), record.getData());
    }

    /**
     * 如果record中的属性为空则不做更新
     *
     * @param tableName
     * @param record
     * @param keys
     * @return
     */
    public int updateSelective(String tableName, Record record, String[] keys) {
        return getNamedParameterJdbcTemplate().update(createUpdateSql(tableName, record, keys, false), record.getData());
    }

    /**
     * 通过表名批量更新
     *
     * @param tableName
     * @param records
     * @param primaryKey
     * @return
     */
    public int[] batchUpdate(String tableName, List<Record> records, String primaryKey) {
        Map<String, Object>[] params = new LinkedCaseInsensitiveMap[records.size()];
        for (int i = 0; i < records.size(); i++) {
            params[i] = records.get(i).getData();
        }
        return getNamedParameterJdbcTemplate().batchUpdate(createUpdateSql(tableName, records.get(0), primaryKey, true), SqlParameterSourceUtils.createBatch(params));
    }

    /**
     * 通过主键批量删除数据
     *
     * @param tableName
     * @param records
     * @param primaryKey
     * @return
     */
    public int[] batchDelete(String tableName, List<?> records, String primaryKey) {
        Map<String, Object>[] params = new LinkedCaseInsensitiveMap[records.size()];
        for (int i = 0; i < records.size(); i++) {
            Map<String, Object> p = new LinkedCaseInsensitiveMap<Object>(1);
            Object t = records.get(i);
            if (t instanceof Record) {
                p.put(primaryKey, ((Record) records.get(i)).get(primaryKey));
            } else {
                p.put(primaryKey, records.get(i));
            }
            params[i] = p;
        }
        return getNamedParameterJdbcTemplate().batchUpdate(new StringBuffer("delete from ").append(tableName).append(" where ").append(getDaoAdapter().getSeparator()).append(primaryKey).append(getDaoAdapter().getSeparator()).append("=:").append(primaryKey).toString()
                , SqlParameterSourceUtils.createBatch(params));
    }


    public int delete(String tableName, Record records, String primaryKey) {
        Object key = records.get(primaryKey);
        if (key == null) {
            throw new ServiceException("删除数据时,主键值不能为空.");
        }
        StringBuffer sql = new StringBuffer(" delete from ").append(tableName).append(" where ").append(primaryKey).append("=:").append(getDaoAdapter().getSeparator()).append(primaryKey).append(getDaoAdapter().getSeparator()).append("=?");
        return getNamedParameterJdbcTemplate().getJdbcTemplate().update(sql.toString(), key);
    }

    /**
     * 查询数据
     */
    public Record query(SqlBuilder sqlBuilder) {
        Map<String, Object> map = executeQuery(()->getNamedParameterJdbcTemplate().queryForMap(sqlBuilder.getSql(getDaoAdapter()), sqlBuilder.getValues()));
        return map == null ? null : new Record(map);
    }


    /**
     * 查询List记录
     */
    protected List<Record> queryList(String sql, SqlBuilder sqlBuilder) {
        return convertRecords(executeQuery(()->getNamedParameterJdbcTemplate().queryForList(sql, sqlBuilder.getValues())));
    }

    /**
     * 查询List记录
     */
    public List<Record> queryList(SqlBuilder sqlBuilder) {
        return queryList(sqlBuilder.getSql(getDaoAdapter()), sqlBuilder);
    }

    /**
     * 查询分页记录
     */
    public Page<Record> queryPage(SqlBuilder sqlBuilder, int pageBeg, int size) {
        Long total = getTotal(sqlBuilder);
        if (total == null || total == 0) {
            return new Page<Record>(null, 0l, size, pageBeg);
        }
        return new Page<Record>(queryList(getDaoAdapter().getPageSql(sqlBuilder.getSql(getDaoAdapter()), pageBeg, size), sqlBuilder), total, size, pageBeg);
    }
}
