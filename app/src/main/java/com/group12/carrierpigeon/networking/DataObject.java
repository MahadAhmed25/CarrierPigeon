package com.group12.carrierpigeon.networking;

import java.io.Serializable;

public class DataObject implements Serializable {
    public enum Status {
        VALID,FAIL;
    }

    private Status status;
    private byte[] data;


    public DataObject(Status status, byte[] data) {
        this.status = status;
        this.data = data;
    }

    public Status getStatus() {
        return this.status;
    }
    public byte[] getData() {
        return this.data;
    }
}
