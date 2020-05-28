package com.jiangbo.savior.adapter;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.callback.ICompatibleCallBack;
import com.jiangbo.savior.pojo.table.TbSaviorTable;
import org.junit.Test;

/**
 * 数据库存适配器测试类
 */
public class DaoAdapterTest extends BaseSaviorTest {

    /**
     * 测试函数适配
     */
    @Test
    public void testFunDaoAdapter(){
        System.out.println(dao.baseTemplate.query(String.class,"select "+dao.adapter.concat("name","id") +"from tb_savior where id=1"));
    }

    /**
     * 根据数据库类型适配
     */
    @Test
    public void testDbTypeDaoAdapter(){
        dao.compatible(new ICompatibleCallBack<TbSaviorTable>() {
            //mysql会执行下面的方法
            @Override
            public TbSaviorTable executeMysql() {
                return dao.tableTemplate.query(TbSaviorTable::new,"select * from tb_savior limit 1");
            }
            //oracle会执行下面的方法
            @Override
            public TbSaviorTable executeOracel() {
                return dao.tableTemplate.query(TbSaviorTable::new,"SELECT * FROM  (SELECT T.* FROM (  select * from tb_savior  ) T   WHERE ROWNUM <= 10 )WHERE ROWNUM >= 1");
            }
        });
    }
}
