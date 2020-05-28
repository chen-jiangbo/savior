package com.jiangbo.savior.builder.limit;

public class Limit {

    private int offset;

    private int size;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Limit(int offset, int size) {
        this.offset = offset;
        this.size = size;
    }

    public Limit(int size) {
        this.offset = 0;
        this.size = size;
    }
}
