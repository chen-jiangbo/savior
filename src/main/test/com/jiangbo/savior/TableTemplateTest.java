package com.jiangbo.savior;

import org.junit.Test;

import java.util.Date;

/**
 * 基础类型数据操作测试类
 */
public class TableTemplateTest extends BaseSaviorTest {

    /**
     * 创建表
     */
    @Test
    public void testCreateTable(){
        dao.langTemplate.execute("CREATE TABLE `tb_savior`(  \n" +
                "  `id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `name` VARCHAR(50) COMMENT '名称',\n" +
                "  `create_time` DATETIME COMMENT '创建时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ")");
    }
    /**
     * 插入表
     */
    @Test
    public void testInserData(){
        dao.langTemplate.insert("INSERT INTO tb_savior (name,create_time) VALUES (?,?)","张三",new Date());
    }

    /**
     * 插入表
     */
    @Test
    public void testDeleteData(){
        dao.langTemplate.delete("tb_savior","name","张三");
    }


}
