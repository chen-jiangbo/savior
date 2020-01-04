package com.jiangbo.savior.coder.template;


import com.jiangbo.savior.coder.config.CoderConfig;
import com.jiangbo.savior.coder.model.Table;

public interface Template {

    public default Template initCoderConfig(CoderConfig coderConfig){
        return this;
    }

    public String parseTemplate(Table table);
}
