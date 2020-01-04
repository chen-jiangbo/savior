package com.jiangbo.savior.callback;

import com.jiangbo.savior.model.BaseTable;
import com.jiangbo.savior.model.Context;

/**
 * 上下文回调接口
 */
public interface IContextCallBack <T extends BaseTable>  {

    public <T> void buildContext(Context context);
}
