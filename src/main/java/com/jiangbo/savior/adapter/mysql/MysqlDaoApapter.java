package com.jiangbo.savior.adapter.mysql;


import com.jiangbo.savior.adapter.DaoAdapter;
import com.jiangbo.savior.adapter.enums.DbTypeEnum;

public class MysqlDaoApapter implements DaoAdapter {
    @Override
    public DbTypeEnum getDbType() {
        return DbTypeEnum.MYSQL;
    }

    @Override
    public String getSeparator() {
        return "`";
    }

    @Override
    public String getPageSql(String sql, int pageBeg, int size){
        return sql+" limit " + (pageBeg == 0 ? 0 : (pageBeg - 1) * size) + "," + size;
    }

    @Override
    public String addLimit(StringBuffer sf, int offset, int size) {
        return sf.append(" limit " + offset + "," + size).toString();
    }

    @Override
    public String concat(String... params) {
        return " CONCAT("+String.join(",", params)+") ";
    }

    @Override
    public String ifNull(String col, String val) {
        return " IFNULL("+col+","+val+") ";
    }

    @Override
    public String subStr(String col, int beg, int end) {
        return " SUBSTRING("+col+", "+beg+", "+end+") ";
    }

    @Override
    public String formateDate(String col) {
        return " DATE_FORMAT("+col+",'%Y-%m-%d') ";
    }

    @Override
    public String formatDateTime(String col) {
        return " DATE_FORMAT("+col+",'%Y-%m-%d %H:%i:%s') ";
    }

    @Override
    public String parseDate(String str) {
        return " STR_TO_DATE('"+str.trim()+"','%Y-%m-%d %H:%i:%s')";
    }
}
