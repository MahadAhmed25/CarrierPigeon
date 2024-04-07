package com.group12.carrierpigeon.components.Data;

public class Data {

    private Object data;
    private Class<?> type;

    public Data(Object data, Class<?> type) {
        this.data = data;
        this.type = type;
    }

    public Class<?> getType() {
        return this.type;
    };

    public Object getData() {
        return this.data;
    }

}
