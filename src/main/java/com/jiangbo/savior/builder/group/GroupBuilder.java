package com.jiangbo.savior.builder.group;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GroupBuilder {

    public static String getSubGroupBySql(List<GroupBy> groupByList) {
        StringBuffer rs = new StringBuffer("");
        if (CollectionUtils.isEmpty(groupByList)) {
            return rs.toString();
        }
        rs.append(" GROUP BY ");
        rs.append(groupByList.stream().filter((o) -> o != null).map(GroupBy::getColumn).collect(Collectors.joining(",")));
        rs.append(" ");
        return rs.toString();
    }
}
