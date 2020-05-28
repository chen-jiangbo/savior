package com.jiangbo.savior.adapter.oracle;


import com.jiangbo.savior.adapter.DaoAdapter;
import com.jiangbo.savior.adapter.enums.DbTypeEnum;

public class OracleDaoApapter implements DaoAdapter {
    @Override
    public DbTypeEnum getDbType() {
        return DbTypeEnum.ORACLE;
    }

    @Override
    public String getSeparator() {
        return " ";
    }

    @Override
    public String getPageSql(String sql, int pageBeg, int size) {
        pageBeg = (pageBeg == 0 ? 0 : (pageBeg - 1) * size);
        return "SELECT * FROM  (SELECT T.* FROM ( " + sql + " ) T   WHERE ROWNUM <= " + (pageBeg + size) + " )WHERE ROWNUM >= " + pageBeg;
    }

    @Override
    public String addLimit(StringBuffer sf, int offset, int size) {
        return "SELECT * FROM  (SELECT T.* FROM ( " + sf.toString() + " ) T   WHERE ROWNUM <= " + (offset + size) + " )WHERE ROWNUM >= " + offset;
    }

    @Override
    public String concat(String... params) {
        return String.join("||",params);
    }

    @Override
    public String ifNull(String col, String val) {
        return " NVL("+col+", "+val+") ";
    }

    @Override
    public String subStr(String col, int beg, int end) {
        return " SUBSTR("+col+","+beg+","+end+") ";
    }

    @Override
    public String formateDate(String col) {
        return " to_char("+col+",'yyyy-MM-dd') ";
    }

    @Override
    public String formatDateTime(String col) {
        return " to_char("+col+",'yyyy-MM-dd HH24:mi:ss') ";
    }

    @Override
    public String parseDate(String str) {
        return " to_date('"+str.trim()+"','yyyy-MM-dd HH24:mi:ss') ";
    }

}
