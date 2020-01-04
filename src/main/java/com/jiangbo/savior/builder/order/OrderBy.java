package com.jiangbo.savior.builder.order;

public class OrderBy {

    private String cloumn;

    private String sort;

    public String getCloumn() {
        return cloumn;
    }

    public String getSort() {
        return sort;
    }

    private OrderBy(String cloumn, String sort) {
        this.cloumn = cloumn;
        this.sort=sort;
    }


    public static OrderBy desc(String column){
          return new OrderBy(column,"desc");
    }

    public static OrderBy asc(String column){
        return new OrderBy(column,"asc");
    }
}
