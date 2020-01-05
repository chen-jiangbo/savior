package com.jiangbo.savior.mapper;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.SaviorDao;
import com.jiangbo.savior.pojo.model.TbSaviorExtendsModel;
import com.jiangbo.savior.pojo.table.TbSaviorTable;
import org.junit.Test;

/**
 * mapper测试类
 */
public class MapperTest extends BaseSaviorTest {

   public class TbSaviorMapper extends BaseMapper<TbSaviorTable,Long>{

       @Override
       protected MapperTableInfo<TbSaviorTable> mapperTableInfo() {
           return new MapperTableInfo<>("tb_savior",TbSaviorTable::new,"id");
       }

       public TbSaviorMapper(SaviorDao dao){
           super(dao);
       }


       public TbSaviorExtendsModel queryModel(){
           return dao.modelTemplate.query((record) -> {
               TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
               rs.convert(record);
               rs.seteStr("......");
               return rs;
           }, "select * from tb_savior where id = 3 limit 1");
       }
   }




   @Test
    public void testQueryTable(){
       TbSaviorMapper tbSaviorMapper=new TbSaviorMapper(this.dao);
       System.out.println(tbSaviorMapper.queryTable(1l).getName());
   }


}
