package com.jiangbo.savior.builder.where;


import com.jiangbo.savior.adapter.DaoAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhereBuilder {
    /**
     * @类名称:getwhereString
     * @类描述:得到where子句，以？为占位符。通过条件集合中所存储的条件进行and拼接的where子句
     * @注意：不能与getwhereOrString同时使用
     * @return：拼接好的where子句
     */
    public static String generWhereSql(DaoAdapter daoAdapter, List<Logical> list) {
        StringBuffer subWhereSql= new StringBuffer("");
        if (list.size() <= 0) {
            return subWhereSql.toString();
        } else {
            subWhereSql.append(" where 1=1 ");
            for (Logical logical : list) {
                if (logical.getCondition() != null) {
                    subWhereSql.append(convertConditionStr(logical.getOperation(), logical.getCondition(),daoAdapter));
                } else {
                    subWhereSql.append(logical.getOperation()).append(" ( ");
                    String lg = " and ".equals(logical.getOperation()) ? " or " : " and ";
                    for (int i = 0; i < logical.getConditionList().size(); i++) {
                        if (i == 0) {
                            subWhereSql.append(convertConditionStr("", logical.getConditionList().get(i),daoAdapter));
                        } else {
                            subWhereSql.append(convertConditionStr(lg, logical.getConditionList().get(i),daoAdapter));
                        }
                    }
                    subWhereSql.append(" ) ");
                }
            }
            return subWhereSql.toString();
        }
    }

    /**
     * 条件转换成sql子句
     *
     * @param item
     */
    public static  StringBuffer convertConditionStr(String logical, Condition item,DaoAdapter daoAdapter) {
        item.setAlias(item.getName(),daoAdapter);
        StringBuffer sb = new StringBuffer(logical);
        switch (item.getOpertion()) {
            case Equal:
                sb.append(item.getName())
                        .append(" = ")
                        .append(item.getAlias());
                break;
            case UnEqual:
                sb.append(item.getName())
                        .append(" <> ")
                        .append(item.getAlias());
                break;
            case GreatThan:
                sb.append(item.getName())
                        .append(" >= ")
                        .append(item.getAlias());
                break;
            case LessThan:
                sb.append(item.getName())
                        .append(" <= ")
                        .append(item.getAlias());
                break;
            case Like:
                sb.append(item.getName())
                        .append(" like ")
                        .append(item.getAlias());
                break;
            case In:
                sb.append(item.getName())
                        .append(" in ")
                        .append(item.getAlias());
                break;
            case NotIn:
                sb.append(item.getName())
                        .append(" not in ")
                        .append(item.getAlias());
                break;
            case appendSql:
                sb.append((String) item.getValue());
                break;
            case IsNull:
            case IsNotNull:
                sb.append(item.getName())
                        .append(item.getAlias());
                break;
        }
        return sb;
    }

    /**
     * @类名称:getwhereValues
     * @类描述:通过条件集合中的条件得到与where字句位置对应的object集合。存储所有的值
     * @return：
     */
    public static Map<String, Object> getValues(List<Logical> list) {
        Map<String, Object> obj = new HashMap<String, Object>();
        for (int i = 0; i < list.size(); i++) {
            Logical logical = list.get(i);
            if (logical.getCondition() != null) {
                if (logical.getCondition().getOpertion().equals(Opt.appendSql)
                        || logical.getCondition().getOpertion().equals(Opt.IsNull)
                        || logical.getCondition().getOpertion().equals(Opt.IsNotNull)
                ) {
                    continue;
                }
                obj.put(logical.getCondition().getAttr(), logical.getCondition().getValue());
            } else {
                for (Condition condition : logical.getConditionList()) {
                    if (condition.getOpertion().equals(Opt.appendSql)
                            || logical.getCondition().getOpertion().equals(Opt.IsNull)
                            || logical.getCondition().getOpertion().equals(Opt.IsNotNull)) {
                        continue;
                    }
                    obj.put(condition.getAttr(), condition.getValue());
                }
            }
        }
        return obj;
    }
}
