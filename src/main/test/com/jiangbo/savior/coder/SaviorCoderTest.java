package com.jiangbo.savior.coder;

import com.jiangbo.savior.BaseSaviorTest;
import com.jiangbo.savior.coder.config.CoderConfig;
import org.junit.Test;

/**
 * 代码生成器测试类
 */
public class SaviorCoderTest extends BaseSaviorTest {

    /**
     * 生成代码文件
     */
    @Test
    public void testGenerateFile() {
        /**
         * String basdDir 生成文件的路径地址
         * String packageName 实体类型的包名
         * String[] tableNames 表名的数组(支持多表同时生成)
         */
        dao.coder.generator("D://","com.jiangbo.savior.model",new String[]{"tb_savior"});

    }

    /**
     * 生成代码字符串
     */
    @Test
    public void testGenerateString() {
        /**
         * String packageName 实体类型的包名
         * String[] tableNames 表名的数组(支持多表同时生成)
         */
        System.out.println(dao.coder.generator("com.jiangbo.savior.model",new String[]{"tb_savior"}));
    }

    /**
     * 测试自定义配置的代码生成规则
     */
    @Test
    public void testGenerateCustomString(){
        CoderConfig coderConfig=new CoderConfig();
        //设置是否生成swagger文档(默认true)
        coderConfig.setGenSwaggerAnnotation(false);
        //设置是否支持Lombok(默认true)
        coderConfig.setSupportLombok(false);
        System.out.println(dao.coder.generator("com.jiangbo.savior.model",new String[]{"tb_savior"},coderConfig));
    }
}
