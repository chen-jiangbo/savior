package com.jiangbo.savior.pojo.model;

import java.util.Date;

/**
 *自定义model
 */
public class TbSaviorCustomModel {
    private java.lang.Long mId;
    private java.lang.String mName;
    private java.util.Date mCreateTime;

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Date getmCreateTime() {
        return mCreateTime;
    }

    public void setmCreateTime(Date mCreateTime) {
        this.mCreateTime = mCreateTime;
    }

    @Override
    public String toString() {
        return "TbSaviorCustomModel{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mCreateTime=" + mCreateTime +
                '}';
    }
}
