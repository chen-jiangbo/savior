package com.jiangbo.savior.callback;

import com.jiangbo.savior.model.Record;

public interface IModelQueryCallback<T> {

    public T buildModel(Record record);
}
