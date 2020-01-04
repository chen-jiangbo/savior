package com.jiangbo.savior.adapter;

import com.jiangbo.savior.adapter.enums.DbTypeEnum;
import com.jiangbo.savior.coder.adapter.Adapter;
import com.jiangbo.savior.coder.adapter.mysql.MysqlAdapter;
import com.jiangbo.savior.coder.adapter.oracle.OracleAdapter;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdapterFactory {

    public static Adapter getAdapter(DbTypeEnum dbTypeEnum, JdbcTemplate jdbcTemplate) {
        switch (dbTypeEnum) {
            case ORACLE:
                return new OracleAdapter(jdbcTemplate);
            case MYSQL:
                return new MysqlAdapter(jdbcTemplate);
            default:
                throw new RuntimeException("暂不支持生成代码的数据库类型!");
        }
    }
}
