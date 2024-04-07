package com.group12.carrierpigeon.components.accounts;

import com.group12.carrierpigeon.components.Data.Data;
import com.group12.carrierpigeon.components.Source;

public class Account extends Source {
    private byte[] ticket;
    private String username;
    private String password;

    public Account (String username, String password) {
        this.username = username;
        this.password = password;
        ticket = null;
    }

    @Override
    public void handleData(Data data) {

    }

    @Override
    public void getData(Data data) {

    }
}
