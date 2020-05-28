package com.jiangbo.savior.builder;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.builder.group.GroupBy;
import com.jiangbo.savior.builder.order.OrderBy;
import com.jiangbo.savior.builder.where.Condition;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.pojo.model.TbSaviorExtendsModel;
import com.jiangbo.savior.pojo.table.TbSaviorTable;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SqlBuilderTest extends BaseSaviorTest {

    /**
     * 通过sqlBuilder查询分页
     */
    @Test
    public void testQueryModelPageBySqlBuilder(){
        SqlBuilder sqlBuilder=SqlBuilder.getSelect("select * from tb_savior");
        sqlBuilder.and(Condition.eq("id",5l));
        sqlBuilder.and(Condition.like("name","%七%"));
        Page<TbSaviorExtendsModel> pageModel = dao.modelTemplate.queryPage((Record record) -> {
            TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
            rs.convert(record);
            rs.seteStr("...........");
            return rs;
        }, sqlBuilder, 1, 10);
        System.out.println("size==>"+pageModel.getTotalCount());
        System.out.println("name==>"+((List<TbSaviorExtendsModel>)pageModel.getList()).get(0).getName());
    }

    /**
     * 通过sqlBuilder查询分页
     */
    @Test
    public void testQueryTablePageBySqlBuilder(){
        SqlBuilder sqlBuilder=SqlBuilder.getSelect("select * from tb_savior");
        sqlBuilder.and(Condition.eq("id",5l));
        sqlBuilder.and(Condition.like("name","%七%"));
        Page<TbSaviorTable> pageModel = dao.tableTemplate.queryPage(TbSaviorTable::new, sqlBuilder, 1, 10);
        System.out.println("size==>"+pageModel.getTotalCount());
        System.out.println("name==>"+((List<TbSaviorExtendsModel>)pageModel.getList()).get(0).getName());
    }

    /**
     * 条件查询测试
     */
    @Test
    public void testSqlBuilderContition(){
        SqlBuilder sqlBuilder=SqlBuilder.getSelect("select * from tb_savior");
        //等于条件
        sqlBuilder.and(Condition.eq("id",5l));
        //不等于
        sqlBuilder.and(Condition.uEq("id",5l));
        //小于
        sqlBuilder.and(Condition.lessThan("id",5l));
        //大于
        sqlBuilder.and(Condition.greatThan("id",5l));
        //包含
        sqlBuilder.and(Condition.in("id", Arrays.asList(5l,6l)));
        //不包含
        sqlBuilder.and(Condition.notIn("id", Arrays.asList(5l,6l)));
        //模糊查询
        sqlBuilder.and(Condition.like("name","%张%"));
        //直接拼接条件值
        sqlBuilder.and(Condition.appendSql("id=4"));
        //多条件要用括号包裹起来优先执行
        //or (id=5 and id=6)
        sqlBuilder.orListAnd(Arrays.asList(Condition.eq("id",5l),Condition.eq("id",6l)));
        //and (id=5 or id=6)
        sqlBuilder.andListOr(Arrays.asList(Condition.eq("id",5l),Condition.eq("id",6l)));
        dao.recordTemplate.queryList(sqlBuilder);
    }

    /**
     * 测试 group by /order by
     */
    @Test
    public void testSqlBuilder(){
        SqlBuilder sqlBuilder=SqlBuilder.getSelect("select name,count(1) as num from tb_savior");
        //增加group
        sqlBuilder.addGroupBy(GroupBy.by("name"));
        //增加order by
        sqlBuilder.addOrderBy(OrderBy.asc("name"));
        dao.recordTemplate.queryPage(sqlBuilder,1,10);
    }
}
