package com.jiangbo.savior.builder.having;

public class Having {

    public String havingSql;

    private Having(String havingSql) {
        this.havingSql = havingSql;
    }

    public String getHavingSql() {
        return havingSql;
    }

    public static Having by(String havingSql){
        return new Having(havingSql);
    }
}
