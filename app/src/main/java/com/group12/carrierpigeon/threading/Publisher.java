package com.group12.carrierpigeon.threading;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public class Publisher<T> {
    private List<Subscriber<T>> subscribers = new ArrayList<>();

    public void subscribe(Subscriber<T> subscriber) {
        this.subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber<T> subscriber) {
        this.subscribers.remove(subscriber);
    }

    /**
     * Notifies subscribers. Execution happens on the main UI thread
     * @param context the type parameter object which passes messages through to {@link Subscriber}s
     */
    public void notifySubscribers(T context, String whoIs) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (Subscriber<T> subscriber : this.subscribers) {
                subscriber.update(context,whoIs);
            }
        });
    }

    /**
     * Notifies subscribers. Execution happens on the same thread as the caller
     * @param context the type parameter object which passes messages through to {@link Subscriber}s
     */
    public void notifySubscribersInSameThread(T context, String whoIs) {
        for (Subscriber<T> subscriber : this.subscribers) {
            subscriber.update(context, whoIs);
        }
    }

    public void unsubscribeAll() {
        this.subscribers = new ArrayList<>();
    }

}
