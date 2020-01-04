package com.jiangbo.savior.pojo.table;
import com.jiangbo.savior.model.Record;
import com.jiangbo.savior.model.BaseTable;

public class TbSaviorTable extends BaseTable{

    public TbSaviorTable(){
        super();
    }
    @Override
    public void convert(Record record) {
        setId(record.getLong("id"));
        setName(record.getString("name"));
        setCreateTime(record.getDate("create_time"));
    }

    @Override
    /**savior框架测试表对象转换成record*/
    public Record reversal(){
        return new Record()
                .setColumn("id",this.getId())
                .setColumn("name",this.getName())
                .setColumn("create_time",this.getCreateTime());    }

    private java.lang.Long id;
    private java.lang.String name;
    private java.util.Date createTime;

    /**获取主键*/
    public java.lang.Long getId(){
        return this.id;
    }
    /**设置主键*/
    public void setId(java.lang.Long id){
        this.id=id;
    }
    /**获取名称*/
    public java.lang.String getName(){
        return this.name;
    }
    /**设置名称*/
    public void setName(java.lang.String name){
        this.name=name;
    }
    /**获取创建时间*/
    public java.util.Date getCreateTime(){
        return this.createTime;
    }
    /**设置创建时间*/
    public void setCreateTime(java.util.Date createTime){
        this.createTime=createTime;
    }

}
