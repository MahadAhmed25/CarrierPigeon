package com.group12.carrierpigeon.threading;

public interface Subscriber<T> {

    /**
     * Method to invoke some form of logic when a {@link Publisher} notifies a subscribed class
     */
    void update(T context);

}
