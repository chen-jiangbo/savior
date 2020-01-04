package com.jiangbo.savior.coder.adapter;

import com.jiangbo.savior.coder.model.Column;
import com.jiangbo.savior.coder.model.Table;
import com.jiangbo.savior.utils.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter implements Adapter {

    private JdbcTemplate jdbcTemplate;
    public BaseAdapter(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }


    private Table getSingleTableInfo(String catalog, DatabaseMetaData metaData, String tableName) throws Exception {
        tableName=tableName.toUpperCase();
        Table t = new Table();
        // 获取表
        ResultSet rs = metaData.getTables(catalog, "%", tableName, new String[]{"TABLE"});
        while (rs.next()) {
            t.setTableName(rs.getString("TABLE_NAME"));
            t.setModleName(getClassName(t.getTableName()));
            t.setTableDesc(rs.getString("REMARKS"));
        }
        ResultSet crs = metaData.getColumns(catalog, "%", tableName, "%");
        List<Column> columnList = new ArrayList<Column>();
        while (crs.next()) {
            Column c = new Column();
            c.setColumnName(crs.getString("COLUMN_NAME").toLowerCase());
            c.setColumnDesc(crs.getString("REMARKS"));
            c.setColumnType(crs.getString("TYPE_NAME"));
            c.setColumnSize(crs.getInt("COLUMN_SIZE"));
            c.setDigits(crs.getInt("DECIMAL_DIGITS"));
            c.setJavaType(getFieldType(c.getColumnType(),c.getColumnSize(),c.getDigits()));
            c.setPropName(StringUtils.camelCase(c.getColumnName().toLowerCase()));
            c.setMethodName(StringUtils.toUpperCaseFirstOne(c.getPropName()));
            columnList.add(c);
        }
        t.setColumns(columnList);
        return t;
    }

    private String getClassName(String columnName) {
        String res = columnName.toLowerCase();
        return StringUtils.toUpperCaseFirstOne(StringUtils.camelCase(res)) + "Table";
    }


    public abstract String getFieldType(String columnType,int columnSize,int digits);

    @Override
    public List<Table> getTablesInfo(String[] tableNames) throws Exception {
        Connection conn = this.jdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData metaData = conn.getMetaData();
        List<Table> tbs = new ArrayList<>(tableNames.length);
        for (String tableName : tableNames) {
            // catalog 其实也就是数据库名
            tbs.add(getSingleTableInfo(conn.getCatalog(), metaData, tableName));
        }
        conn.close();
        return tbs;
    }
}
