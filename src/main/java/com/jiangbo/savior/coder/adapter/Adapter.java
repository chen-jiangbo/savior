package com.jiangbo.savior.coder.adapter;


import com.jiangbo.savior.coder.model.Table;

import java.util.List;

public interface Adapter {


    /**
     * 获取表的元信息
     * @param tableNames
     * @return
     */
    public List<Table> getTablesInfo(String[] tableNames) throws Exception;
}
