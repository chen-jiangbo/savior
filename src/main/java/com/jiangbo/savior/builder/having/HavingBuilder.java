package com.jiangbo.savior.builder.having;

public class HavingBuilder {

    public static String getSubOrderBySql(Having having) {
        StringBuffer rs = new StringBuffer("");
        if (having==null) {
            return rs.toString();
        }
        rs.append(" HAVING ");
        rs.append(having.getHavingSql());
        rs.append(" ");
        return rs.toString();
    }
}
