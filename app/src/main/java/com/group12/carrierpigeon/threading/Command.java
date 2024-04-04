package com.group12.carrierpigeon.threading;

public interface Command {

    /**
     * Some function to perform logic in. Typically executed asynchronously from the main UI thread by a {@link Worker}
     */
    public void performAction();

}
