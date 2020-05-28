package com.jiangbo.savior.template;

import com.jiangbo.savior.BaseSaviorTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

/**
 * 基础类型数据操作测试类
 */
public class BaseTemplateTest extends BaseSaviorTest {

    /**
     * 创建表
     */
    @Test
    public void testCreateTable() {
        dao.baseTemplate.execute("CREATE TABLE `tb_savior`(  \n" +
                "  `id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `name` VARCHAR(50) COMMENT '名称',\n" +
                "  `create_time` DATETIME COMMENT '创建时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") COMMENT='savior框架测试表' ");
    }

    /**
     * 插入表数据
     */
    @Test
    public void testInsertData() {
        dao.baseTemplate.insert("INSERT INTO tb_savior (id,name,create_time) VALUES (1,?,?)", "张三", new Date());
    }

    /**
     * 删除表数据
     */
    @Test
    public void testDeleteData() {
        dao.baseTemplate.delete("tb_savior", "name", "张三");
    }


    /**
     * 查询单条数据
     */
    @Test
    public void testQueryData() {
        System.out.println(dao.baseTemplate.query(String.class, "select name from tb_savior where id=1"));
    }

    /**
     * 测试查询列表
     */
    @Test
    public void testQueryListData() {
        System.out.println(Arrays.toString(dao.baseTemplate.queryList(String.class, "select name from tb_savior").toArray()));
    }
}
