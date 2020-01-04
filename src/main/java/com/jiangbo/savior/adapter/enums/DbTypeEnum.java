package com.jiangbo.savior.adapter.enums;


import com.jiangbo.savior.adapter.DaoAdapter;
import com.jiangbo.savior.adapter.mysql.MysqlDaoApapter;
import com.jiangbo.savior.adapter.oracle.OracleDaoApapter;

public enum DbTypeEnum {
    MYSQL(new MysqlDaoApapter()),ORACLE(new OracleDaoApapter());

    private DaoAdapter daoAdapter;

    private DbTypeEnum(DaoAdapter daoAdapter){
        this.daoAdapter=daoAdapter;
    }

    public DaoAdapter getDaoAdapter(){
        return this.daoAdapter;
    }
}
