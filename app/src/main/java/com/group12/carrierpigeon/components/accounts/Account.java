package com.group12.carrierpigeon.components.accounts;

public class Account {

    private byte[] ticket;
    private String username;
    private String password;

    public Account (String username, String password) {
        this.username = username;
        this.password = password;
        ticket = null;
    }

    public byte[] getTicket() {
        return this.ticket;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
