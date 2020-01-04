package com.jiangbo.savior.coder;

import com.jiangbo.savior.adapter.AdapterFactory;
import com.jiangbo.savior.adapter.enums.DbTypeEnum;
import com.jiangbo.savior.coder.template.TableTemplate;
import com.jiangbo.savior.coder.config.CoderConfig;
import com.jiangbo.savior.coder.file.FileGenerator;
import com.jiangbo.savior.coder.model.Table;
import com.jiangbo.savior.utils.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class Coder {

    /**
     * 是否已经使用了自己定义的数据库连接池
     */
    private boolean isUseCustomerDataSource = false;
    /**
     * jdbc连接模板
     */
    private JdbcTemplate jdbcTemplate;
    /**
     * 数据库存类型
     */
    private DbTypeEnum dbTypeEnum;

    public Coder(JdbcTemplate jdbcTemplate, DbTypeEnum dbTypeEnum) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbTypeEnum = dbTypeEnum;
    }

    /**
     * 生成table实体类
     *
     * @param basdDir     生成文件路径
     * @param packageName table文件包名
     * @param tableNames  数据库表名集合
     */
    public void generator(String basdDir, String packageName, String[] tableNames) {
        generator(basdDir, packageName, tableNames, new CoderConfig());
    }

    /**
     * 生成table实体类
     *
     * @param packageName table文件包名
     * @param tableNames  数据库表名集合
     */
    public String generator(String packageName, String[] tableNames) {
        return generator(packageName, tableNames, new CoderConfig());
    }

    /**
     * 生成table实体类
     *
     * @param packageName table文件包名
     * @param tableNames  数据库表名集合
     * @param coderConfig 代码生成器配置类
     */
    public String generator(String packageName, String[] tableNames, CoderConfig coderConfig) {
        try {
            List<Table> tbs = AdapterFactory.getAdapter(this.dbTypeEnum, checkCustomerDataSource(coderConfig)).getTablesInfo(tableNames);
            StringBuffer rs = new StringBuffer();
            String content = null;
            for (Table tb : tbs) {
                tb.setPackageName(packageName);
                content = (new TableTemplate().initCoderConfig(coderConfig)).parseTemplate(tb);
                if (StringUtils.isNotBlank(content)) {
                    rs.append("================================" + tb.getModleName() + ".java========================================\n");
                    rs.append(content + "\n\n\n");
                }
            }
            return rs.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成table实体类
     *
     * @param basdDir     生成文件路径
     * @param packageName table文件包名
     * @param tableNames  数据库表名集合
     * @param coderConfig 代码生成器配置类
     */
    public void generator(String basdDir, String packageName, String[] tableNames, CoderConfig coderConfig) {
        try {
            coderConfig = (coderConfig == null ? new CoderConfig() : coderConfig);
            List<Table> tbs = AdapterFactory.getAdapter(this.dbTypeEnum, checkCustomerDataSource(coderConfig)).getTablesInfo(tableNames);
            for (Table tb : tbs) {
                tb.setPackageName(packageName);
                //创建文件
                FileGenerator.generateFile(basdDir, tb.getModleName(), (new TableTemplate().initCoderConfig(coderConfig)).parseTemplate(tb));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查自定义连接池
     *
     * @param coderConfig
     * @return
     */
    private JdbcTemplate checkCustomerDataSource(CoderConfig coderConfig) {
        if (coderConfig.getDataSource() == null) {
            return this.jdbcTemplate;
        }
        if (this.isUseCustomerDataSource) {
            return this.jdbcTemplate;
        }
        this.jdbcTemplate = new JdbcTemplate(coderConfig.getDataSource());
        this.isUseCustomerDataSource = true;
        return this.jdbcTemplate;
    }

}
