package com.group12.carrierpigeon.threading;

public interface ReturnCommand<T> {

    /**
     * A command with some arbitrary logic where the return value matters. Typically used by a {@link Worker} class.
     * @return the given return value for the logic
     */
    public T performAction();

}
