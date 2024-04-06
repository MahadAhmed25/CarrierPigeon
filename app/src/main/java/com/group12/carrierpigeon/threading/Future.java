package com.group12.carrierpigeon.threading;

/**
 * A Future class regulates return value calls from asynchronous sources. It will block calling threads if a return value is not ready.
 * @param <T>
 */
public class Future<T> extends Publisher<T> {

    private T returnObject = null;

    /**
     * Gets the returned object from a Worker class. If the return is not valid yet, this method will block until such return is valid. Method is thread-safe.
     * If the caller does not want to risk being blocked, subscribe to the class via a call to {@link Publisher#subscribe(Subscriber)}. {@link Subscriber} class will be notified via execution
     * on the main (UI) thread
     * @return the given Object from a Worker class
     * @throws InterruptedException if the thread becomes blocked and is interrupted
     */
    public synchronized T getReturnObject() throws InterruptedException {
        while(this.returnObject == null) {
            this.wait();
        }
        return this.returnObject;
    }

    /**
     * Sets the return object. Typically performed by the {@link Worker} object which issued this Future object.
     * Once the object is set, all blocked threads on {@link Future#getReturnObject()} will become unblocked. This will also invoke {@link Subscriber#update(Object)} to any
     * subscribers of the {@link Future} object within the main (UI) thread
     * @param returnObject the given object to allow other threads to obtain.
     */
    public synchronized void setReturnObject(T returnObject) {
        this.returnObject = returnObject;
        this.notifyAll();
        this.notifySubscribers(returnObject);
    }


}
