package com.jiangbo.savior.coder.adapter.oracle;

import com.jiangbo.savior.coder.adapter.BaseAdapter;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleAdapter extends BaseAdapter {

    public OracleAdapter(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public String getFieldType(String sqlType, int columnSize, int digits) {
        sqlType=sqlType.toLowerCase();
        if (sqlType.equals("integer")) {
            return "java.lang.Integer";
        } else if (sqlType.equals("long")) {
            return "java.lang.Long";
        } else if (sqlType.equals("float")
                || sqlType.equals("float precision")
                || sqlType.equals("double")
                || sqlType.equals("double precision")
                ) {
            return "java.math.BigDecimal";
        }else if (sqlType.equals("number")
                ||sqlType.equals("decimal")
                || sqlType.equals("numeric")
                || sqlType.equals("real")) {
            return digits==0? (columnSize<10? "java.lang.Integer" : "java.lang.Long") : "java.math.BigDecimal";
        }else if (sqlType.equals("varchar")
                || sqlType.equals("varchar2")
                || sqlType.equals("char")
                || sqlType.equals("nvarchar")
                || sqlType.equals("nchar")) {
            return "java.lang.String";
        } else if (sqlType.equals("datetime")
                || sqlType.equals("date")
                || sqlType.equals("timestamp")) {
            return "java.util.Date";
        }
        return null;
    }
}
