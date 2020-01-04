package com.jiangbo.savior.adapter;


import com.jiangbo.savior.adapter.enums.DbTypeEnum;

public interface DaoAdapter{

    public DbTypeEnum getDbType();

    /**
     * 分隔符号
     * @return
     */
    public String getSeparator();

    /**
     * 分页SQL
     * @return
     */
    public String getPageSql(String sql, int pageBeg, int size);

    /**
     * 参数连接
     * @param params
     * @return
     */
    public String concat(String... params);

    /**
     * 参数中ifnull判断
     * @param col
     * @param val
     * @return
     */
    public String ifNull(String col, String val);

    /**
     * 截断字符中
     * @param col
     * @return
     */
    public String subStr(String col, int beg, int end);
    /**
     * 格式化日期
     * @param col
     * @return
     */
    public String formateDate(String col);

    /**
     * 格式化时间
     * @param col
     * @return
     */
    public String formatDateTime(String col);


    /**
     * String转日期类型
     * @param str
     * @return
     */
    public String parseDate(String str);
}
