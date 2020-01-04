package com.jiangbo.savior.builder;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.builder.where.Condition;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.pojo.model.TbSaviorExtendsModel;
import com.jiangbo.savior.pojo.table.TbSaviorTable;
import org.junit.Test;

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
}
