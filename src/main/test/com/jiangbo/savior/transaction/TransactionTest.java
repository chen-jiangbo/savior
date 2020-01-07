package com.jiangbo.savior.transaction;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.pojo.model.TbSaviorExtendsModel;
import com.jiangbo.savior.pojo.table.TbSaviorTable;
import org.junit.Test;

import java.util.Date;

public class TransactionTest extends BaseSaviorTest {

    private TbSaviorExtendsModel initExtendsModel(){
        TbSaviorExtendsModel extendsModel=new TbSaviorExtendsModel();
        extendsModel.setId(4l);
        extendsModel.setName("李六");
        extendsModel.seteStr("....");
        extendsModel.setCreateTime(new Date());
        return extendsModel;

    }
    private TbSaviorTable initTbSaviorTable() {
        TbSaviorTable rs = new TbSaviorTable();
        rs.setId(5l);
        rs.setName("姚七");
        rs.setCreateTime(new Date());
        return rs;
    }

    @Test
    public void testTransaction() {
        dao.tx(() -> {
            dao.tableTemplate.update("tb_savior", initTbSaviorTable(), "id");
            dao.modelTemplate.update("tb_savior", initExtendsModel(), (model) -> model.reversal(), "id");
        });
    }
}
