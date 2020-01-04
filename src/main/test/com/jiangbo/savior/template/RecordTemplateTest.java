package com.jiangbo.savior.template;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 基础类型数据操作测试类
 */
public class RecordTemplateTest extends BaseSaviorTest {

    private Record record=new Record()
            .setColumn("id",2)
            .setColumn("name","李四")
            .setColumn("create_time",new Date());


    /**
     * 插入Record
     */
    @Test
    public void testInsertRecord(){
        //如果是要返回数据库自动生成的key 需要调用 insertReturnGeneratedKey
        dao.recordTemplate.insert("tb_savior",record);
    }

    /**
     * 更新数据
     */
    @Test
    public void testUpdateRecord(){
        dao.recordTemplate.update("tb_savior",record,"id");
    }

    /**
     * 多条件更新数据(多个条件之间and连接)
     */
    @Test
    public void testUpdateRecordBMultipleKey(){
        dao.recordTemplate.update("tb_savior",record, new String[]{"id","name"});
    }

    /**
     * 查询单条数据
     */
    @Test
    public void testQueryRecord(){
        Record query = dao.recordTemplate.query("select * from tb_savior where id = 2 limit 1");
        System.out.println("name==>"+query.getString("name"));
        System.out.println("id==>"+query.getLong("id"));
    }

    /**
     * 查询集合
     */
    @Test
    public void testQueryListRecord(){
        List<Record> querys = dao.recordTemplate.queryList("select * from tb_savior where id = 2");
        System.out.println("size==>"+querys.size());
        System.out.println("name==>"+querys.get(0).getString("name"));
    }


    /**
     * 查询分页
     */
    @Test
    public void testQueryPageRecord(){
        Page<Record> querys = dao.recordTemplate.queryPage("select * from tb_savior where id = 2" ,1,10);
        System.out.println("size==>"+querys.getTotalCount());
        System.out.println("name==>"+((List<Record>)querys.getList()).get(0).getString("name"));
    }


    /**
     * 删除数据
     */
    @Test
    public void testDeleteRecord(){
        /**
         * String tableName 表名
         * Record records map数据
         * String primaryKey 用于删除时使用的key(只要与这个key值相同的数据都会被删除)
         */
        dao.recordTemplate.delete("tb_savior",record,"id");
    }

    /**
     * 批量删除数据
     */
    @Test
    public void testBatchDeleteRecord(){
        /**
         * String tableName 表名
         * List<?> records map集合数据
         * String primaryKey 用于删除时使用的key(只要与这个key值相同的数据都会被删除)
         */
        dao.recordTemplate.batchDelete("tb_savior", Arrays.asList(record),"id");
    }

}
