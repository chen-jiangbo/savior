package com.jiangbo.savior.callback;

import com.jiangbo.savior.model.Record;

public interface IModelUpdateCallBack <T> {

    public Record buildRecord(Record record, T model);
}
