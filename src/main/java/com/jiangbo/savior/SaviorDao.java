package com.jiangbo.savior;

import com.jiangbo.savior.adapter.DaoAdapter;
import com.jiangbo.savior.adapter.enums.DbTypeEnum;
import com.jiangbo.savior.exception.ServiceException;
import com.jiangbo.savior.template.RecordTemplate;
import com.jiangbo.savior.callback.FCallBack;
import com.jiangbo.savior.coder.Coder;
import com.jiangbo.savior.template.BaseTemplate;
import com.jiangbo.savior.template.ModelTemplate;
import com.jiangbo.savior.template.TableTemplate;
import com.jiangbo.savior.callback.ICompatibleCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.function.Supplier;

public class SaviorDao {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private TransactionTemplate transactionTemplate;
    private DbTypeEnum dbTypeEnum;

    public SaviorDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, TransactionTemplate transactionTemplate, DbTypeEnum dbTypeEnum) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.dbTypeEnum = dbTypeEnum;
        coder = new Coder(this.namedParameterJdbcTemplate.getJdbcTemplate(), this.dbTypeEnum);
        baseTemplate = new BaseTemplate(this.namedParameterJdbcTemplate, this.dbTypeEnum);
        recordTemplate = new RecordTemplate(baseTemplate);
        modelTemplate = new ModelTemplate(recordTemplate);
        tableTemplate = new TableTemplate(recordTemplate);
        adapter = this.dbTypeEnum.getDaoAdapter();
    }

    public SaviorDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DbTypeEnum dbTypeEnum) {
        this(namedParameterJdbcTemplate,new TransactionTemplate(new DataSourceTransactionManager(namedParameterJdbcTemplate.getJdbcTemplate().getDataSource())),dbTypeEnum);
    }

    public SaviorDao(DataSource dataSource, DbTypeEnum dbTypeEnum) {
        this(new NamedParameterJdbcTemplate(dataSource),new TransactionTemplate(new DataSourceTransactionManager(dataSource)),dbTypeEnum);
    }

    /**
     * 代码生成器
     */
    public Coder coder;
    /**
     * 基础类型template
     */
    public BaseTemplate baseTemplate;
    /**
     * Record类型template
     */
    public RecordTemplate recordTemplate;
    /**
     * Model类型template
     */
    public ModelTemplate modelTemplate;
    /**
     * Table类型template
     */
    public TableTemplate tableTemplate;
    /**
     * sql兼容适配器
     */
    public DaoAdapter adapter;


    /**
     * 数据库兼容执行方法
     *
     * @param compatibleCallBack
     * @param <T>
     * @return
     */
    public <T> T compatible(ICompatibleCallBack<T> compatibleCallBack) {
        switch (dbTypeEnum) {
            case ORACLE:
                return compatibleCallBack.executeOracel();
            case MYSQL:
            default:
                return compatibleCallBack.executeMysql();
        }
    }

    /**
     * 数据库事务方法
     * @param <T>
     * @return
     */
    public <T> T tx(Supplier<T> supplier) {
        return transactionTemplate.execute((transactionStatus) -> {
            try {
                return supplier.get();
            } catch (Throwable e) {
                logger.error("数据库执行异常:", e);
                throw new ServiceException(e.getMessage());
            }
        });
    }

    public void tx(FCallBack callBack) {
        transactionTemplate.execute((transactionStatus) -> {
            try {
                callBack.execute();
                return null;
            } catch (Throwable e) {
                logger.error("数据库执行异常:", e);
                throw new ServiceException(e.getMessage());
            }
        });
    }

}
