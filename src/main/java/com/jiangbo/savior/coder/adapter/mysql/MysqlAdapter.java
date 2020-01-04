package com.jiangbo.savior.coder.adapter.mysql;

import com.jiangbo.savior.coder.adapter.BaseAdapter;
import org.springframework.jdbc.core.JdbcTemplate;

public class MysqlAdapter extends BaseAdapter {


    public MysqlAdapter(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * 设置字段类型 MySql数据类型
     * @param columnType 列类型字符串
     * @return
     */
    @Override
    public String getFieldType(String columnType, int columnSize, int digits) {
        columnType = columnType.toLowerCase();
        if (columnType.equals("varchar")
                || columnType.equals("nvarchar")
                || columnType.equals("char")
                ) {
            return "java.lang.String";
        } else if (columnType.equals("tinyblob")
                || columnType.equals("blob")
                || columnType.equals("mediumblob")
                || columnType.equals("longblob")) {
            return "byte[]";
        } else if (columnType.equals("datetime")
                || columnType.equals("date")
                || columnType.equals("timestamp")
                || columnType.equals("time")
                || columnType.equals("year")) {
            return "java.util.Date";
        } else if (columnType.equals("bit")
                || columnType.equals("int")
                || columnType.equals("tinyint")
                || columnType.equals("smallint")
                ) {
            return "java.lang.Integer";
        } else if (columnType.equals("float")) {
            return "java.lang.Float";
        } else if (columnType.equals("double")) {
            return "java.lang.Double";
        } else if (columnType.equals("decimal")) {
            return "java.math.BigDecimal";
        } else if (columnType.equals("bigint")) {
            return "java.lang.Long";
        }
        return "java.lang.String";
    }
}
