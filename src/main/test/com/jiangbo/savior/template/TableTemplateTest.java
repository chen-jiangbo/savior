package com.jiangbo.savior.template;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.pojo.table.TbSaviorTable;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 基础类型数据操作测试类
 */
public class TableTemplateTest extends BaseSaviorTest {

    private TbSaviorTable initTbSaviorTable() {
        TbSaviorTable rs = new TbSaviorTable();
        rs.setId(5l);
        rs.setName("姚七");
        rs.setCreateTime(new Date());
        return rs;
    }

    /**
     * 插入数据
     */
    @Test
    public void testInsertTable() {
        //如果是要返回数据库自动生成的key 需要调用 insertReturnGeneratedKey
        dao.tableTemplate.insert("tb_savior", initTbSaviorTable());
    }

    /**
     * 更新数据
     */
    @Test
    public void testUpdateTable() {
        dao.tableTemplate.update("tb_savior", initTbSaviorTable(), "id");
    }

    /**
     * 多条件更新数据(多个条件之间and连接)
     */
    @Test
    public void testUpdateTableBMultipleKey() {
        dao.tableTemplate.update("tb_savior", initTbSaviorTable(), new String[]{"id", "name"});
    }

    /**
     * 查询单条数据
     */
    @Test
    public void testQueryTable() {
        TbSaviorTable query = dao.tableTemplate.query(TbSaviorTable::new, "select * from tb_savior where id = 5 limit 1");
        System.out.println("name==>" + query.getName());
        System.out.println("id==>" + query.getId());
    }

    /**
     * 查询集合
     */
    @Test
    public void testQueryListRecord() {
        List<TbSaviorTable> querys = dao.tableTemplate.queryList(TbSaviorTable::new, "select * from tb_savior where id = 5");
        System.out.println("size==>" + querys.size());
        System.out.println("name==>" + querys.get(0).getName());
    }


    /**
     * 查询分页
     */
    @Test
    public void testQueryPageTable() {
        Page<TbSaviorTable> querys = dao.tableTemplate.queryPage(TbSaviorTable::new, "select * from tb_savior where id = 5", 1, 10);
        System.out.println("size==>" + querys.getTotalCount());
        System.out.println("name==>" + ((List<TbSaviorTable>) querys.getList()).get(0).getName());
    }


    /**
     * 删除数据
     */
    @Test
    public void testDeleteTable() {
        /**
         * String tableName 表名
         * Record records map数据
         * String primaryKey 用于删除时使用的key(只要与这个key值相同的数据都会被删除)
         */
        dao.tableTemplate.delete("tb_savior", initTbSaviorTable(), "id");
    }

    /**
     * 批量删除数据
     */
    @Test
    public void testBatchDeleteTable() {
        /**
         * String tableName 表名
         * List<?> records map集合数据
         * String primaryKey 用于删除时使用的key(只要与这个key值相同的数据都会被删除)
         */
        dao.recordTemplate.batchDelete("tb_savior", Arrays.asList(initTbSaviorTable()), "id");
    }
}
