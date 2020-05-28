package com.jiangbo.savior;


import com.alibaba.druid.pool.DruidDataSource;
import com.jiangbo.savior.adapter.enums.DbTypeEnum;
import org.junit.After;
import org.junit.Before;

public class BaseSaviorTest {
    /**
     * 数据库连接池
     */
    private DruidDataSource dataSource;
    /**
     * dao操作类
     */
    protected SaviorDao dao;

    private void initDataSource() {
        try {
            this.dataSource = new DruidDataSource();
            this.dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            this.dataSource.setUrl("jdbc:mysql://192.168.2.111:3306/test?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&generateSimpleParameterMetadata=true&useInformationSchema=true");
            this.dataSource.setUsername("jenkin");
            this.dataSource.setPassword("jenkin");
            this.dataSource.setInitialSize(1);
            this.dataSource.setMinIdle(1);
            this.dataSource.setMaxActive(1);
            this.dataSource.setMaxActive(60000);
            this.dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
            this.dataSource.init();
            System.out.println("数据库连接池初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Before
    public void initDao() {
        initDataSource();
        this.dao = new SaviorDao(this.dataSource, DbTypeEnum.MYSQL);
    }

    @After
    public void closeDataSource() {
        this.dataSource.close();
        System.out.println("数据库连接池已关闭");
    }
}
