package com.jiangbo.savior.template;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.model.Page;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.pojo.model.TbSaviorCustomModel;
import com.jiangbo.savior.pojo.model.TbSaviorExtendsModel;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * 基础类型数据操作测试类
 */
public class ModelTemplateTest extends BaseSaviorTest {

    private TbSaviorExtendsModel initExtendsModel(){
        TbSaviorExtendsModel extendsModel=new TbSaviorExtendsModel();
        extendsModel.setId(4l);
        extendsModel.setName("李六");
        extendsModel.seteStr("....");
        extendsModel.setCreateTime(new Date());
        return extendsModel;

    }

    private TbSaviorCustomModel initCustomModel(){
        TbSaviorCustomModel customModel=new TbSaviorCustomModel();
        customModel.setmId(3l);
        customModel.setmName("王五");
        customModel.setmCreateTime(new Date());
        return customModel;
    }

    /**
     * 插入数据
     */
    @Test
    public void testInsertCustomModel() {
        //如果是要返回数据库自动生成的key 需要调用 insertReturnGeneratedKey
        dao.modelTemplate.insert("tb_savior",
                (TbSaviorCustomModel model) -> new Record()
                                .setColumn("id", model.getmId())
                                .setColumn("name", model.getmName())
                                .setColumn("create_time", model.getmCreateTime()),
                initCustomModel());
    }

    /**
     * 插入数据
     */
    @Test
    public void testInsertExtendsModel() {
        //如果是要返回数据库自动生成的key 需要调用 insertReturnGeneratedKey
        dao.modelTemplate.insert("tb_savior",
                (TbSaviorExtendsModel model) -> model.reversal(),
                initExtendsModel());
    }

    /**
     * 更新数据
     */
    @Test
    public void testUpdateRecord() {
        dao.modelTemplate.update("tb_savior",
                initCustomModel(),
                (TbSaviorCustomModel model) -> new Record()
                                .setColumn("id", model.getmId())
                                .setColumn("name", model.getmName())
                                .setColumn("create_time", model.getmCreateTime()),
                "id"
        );
    }

    /**
     * 多条件更新数据(多个条件之间and连接)
     */
    @Test
    public void testUpdateRecordBMultipleKey(){
        dao.modelTemplate.update("tb_savior",
                initCustomModel(),
                (TbSaviorCustomModel model) -> new Record()
                        .setColumn("id", model.getmId())
                        .setColumn("name", model.getmName())
                        .setColumn("create_time", model.getmCreateTime()),
                new String[]{"id","name"}
        );
    }

    /**
     * 查询单条数据
     */
    @Test
    public void testQueryCustomeModel() {
        TbSaviorCustomModel query = dao.modelTemplate.query((record) -> {
            TbSaviorCustomModel rs = new TbSaviorCustomModel();
            rs.setmId(record.getLong("id"));
            rs.setmName(record.getString("name"));
            rs.setmCreateTime(record.getDate("create_time"));
            return rs;
        }, "select * from tb_savior where id = 3 limit 1");
        System.out.println("name==>" + query.getmName());
        System.out.println("id==>" + query.getmId());
    }

    /**
     * 查询单条数据
     */
    @Test
    public void testQueryExtendsModel() {
        TbSaviorExtendsModel query = dao.modelTemplate.query((record) -> {
            TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
            rs.convert(record);
            rs.seteStr("......");
            return rs;
        }, "select * from tb_savior where id = 3 limit 1");
        System.out.println("name==>" + query.getName());
        System.out.println("id==>" + query.getId());
    }

    /**
     * 查询集合
     */
    @Test
    public void testQueryListModel(){
        List<TbSaviorExtendsModel> querys = dao.modelTemplate.queryList((record) -> {
            TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
            rs.convert(record);
            rs.seteStr("......");
            return rs;
        },"select * from tb_savior where id = 4");
        System.out.println("size==>"+querys.size());
        System.out.println("name==>"+querys.get(0).getName());
    }


    /**
     * 查询分页
     */
    @Test
    public void testQueryPageModel(){
        Page<TbSaviorExtendsModel> querys = dao.modelTemplate.queryPage((record)->{
            TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
            rs.convert(record);
            rs.seteStr("......");
            return rs;
        },"select * from tb_savior where id = 4" ,1,10);
        System.out.println("size==>"+querys.getTotalCount());
        System.out.println("name==>"+((List<TbSaviorExtendsModel>)querys.getList()).get(0).getName());
    }

}
