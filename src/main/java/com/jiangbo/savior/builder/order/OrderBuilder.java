package com.jiangbo.savior.builder.order;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderBuilder {

    public static String getSubOrderBySql(List<OrderBy> orderByList) {
        StringBuffer rs = new StringBuffer("");
        if (CollectionUtils.isEmpty(orderByList)) {
            return rs.toString();
        }
        rs.append(" ORDER BY ");
        rs.append(orderByList.stream().filter((o) -> o != null).map((o)->o.getCloumn()+" "+o.getSort()).collect(Collectors.joining(",")));
        rs.append(" ");
        return rs.toString();
    }
}
