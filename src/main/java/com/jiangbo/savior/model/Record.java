package com.jiangbo.savior.model;

import com.jiangbo.savior.exception.TableTransException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Map;

/**
 * 数据库记录的实体类
 */
public class Record extends Container {
    /**
     * 日志类
     */
    protected Logger logger = LoggerFactory.getLogger(Record.class);

    public Record() {
        super();
    }

    public Record setColumn(String column, Object val) {
        getData().put(column, val);
        return this;
    }

    public Record(Map<String,Object> data) {
        super();
        if (data != null) {
            this.addAll(data);
        }
    }

    /**
     * 把record转换成实体对象
     * @param <T>
     * @return
     */
    public <T extends BaseTable> T convertTable(T table) {
        try {
            table.createTable(this);
        } catch (Exception e) {
            logger.error("Data Convert Error:", e);
            throw new TableTransException("Data Convert Error!");
        }
        return table;
    }

    @Override
    public Map<String, Object> initData() {
        return new LinkedCaseInsensitiveMap<Object>();
    }
}
