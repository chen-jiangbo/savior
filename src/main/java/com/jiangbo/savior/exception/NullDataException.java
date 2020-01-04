package com.jiangbo.savior.exception;

public class NullDataException extends RuntimeException {


    public NullDataException(String msg){
        super(msg);
    }

    public NullDataException(){
        super();
    }
}
