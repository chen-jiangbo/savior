package com.jiangbo.savior.pojo.model;

import com.jiangbo.savior.pojo.table.TbSaviorTable;

/**
 *继承自实体类的model
 */
public class TbSaviorExtendsModel extends TbSaviorTable {
    private String eStr;

    public String geteStr() {
        return eStr;
    }

    public void seteStr(String eStr) {
        this.eStr = eStr;
    }
}
