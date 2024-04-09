package com.group12.carrierpigeon.threading;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A Worker class executes Command functions asynchronously. All public methods are thread-safe.
 * @param <T> the type of return the programmer is expecting for a given set of commands
 */
public class Worker<T> {
    private class WorkerThread extends Thread {
        public WorkerThread() {
            this.start();
        }
        @Override
        public void run() {
            try {
                while(!this.isInterrupted()) {
                    performNextCommand();
                }
            } catch (InterruptedException e) {

            }
        }
    }

    private class FutureCommandTuple<T> {
        private Future<T> futureObject;
        private ReturnCommand<T> command;

        public FutureCommandTuple(Future<T> futureObject, ReturnCommand<T> returnCommand) {
            this.futureObject = futureObject;
            this.command = returnCommand;
        }

        public Future<T> getFutureObject() {
            return futureObject;
        }
        public ReturnCommand<T> getCommand() {
            return command;
        }
    }

    private Queue<Command> workQueue;
    private Queue<FutureCommandTuple<T>> workReturnQueue;
    private WorkerThread workerThread;

    public Worker() {
        this.workQueue = new LinkedList<>();
        this.workReturnQueue = new LinkedList<>();
        this.workerThread = new WorkerThread();
    }

    /**
     * Adds a command to be processed. Commands are processed in a first-come-first-serve order.
     * @param command the given {@link Command} function to execute after some time period.
     */
    public synchronized void addCommand(Command command) {
        this.workQueue.add(command);
        this.notify();
    }

    /**
     * Adds a command with a return value to be processed. Commands are processed in a first-come-first-serve order.
     * @param command the given ReturnCommand
     * @return a {@link Future} object with type parameter that is the same as the return value. The caller can access the return value once the {@link Worker} has finished processing it.
     */
    public synchronized Future<T> addReturnCommand(ReturnCommand<T> command) {
        Future<T> future = new Future<>();
        FutureCommandTuple<T> tuple = new FutureCommandTuple<>(future,command);
        this.workReturnQueue.add(tuple);
        this.notify();
        return future;
    }

    public synchronized Future<T> addReturnCommand(ReturnCommand<T> command, String whoIs) {
        Future<T> future = new Future<>();
        future.setWhoIs(whoIs);
        FutureCommandTuple<T> tuple = new FutureCommandTuple<>(future,command);
        this.workReturnQueue.add(tuple);
        this.notify();
        return future;
    }

    // Method for WorkerThread class to perform the next commands in both queues
    private void performNextCommand() throws InterruptedException {
        synchronized (this) {
            while(this.workQueue.peek() == null && this.workReturnQueue.peek() == null) {
                // If the queue is empty, block until another thread calls notify() or KillWorker() (which will throw an InterruptedException)
                this.wait();
            }
        }

        Command command = null;

        synchronized (this) {
            if (this.workQueue.peek() != null) {
                command = this.workQueue.remove();
            }
        }

        // Only synchronize access to the queues as other threads can continue to use the Worker class while the worker performs some command
        if (command != null) {
            command.performAction();
        }

        FutureCommandTuple<T> tuple = null;

        synchronized (this) {
            if (this.workReturnQueue.peek() != null) {
                tuple = this.workReturnQueue.remove();
            }
        }

        if (tuple != null) {
            T returnObject = tuple.getCommand().performAction();
            tuple.getFutureObject().setReturnObject(returnObject);
        }

    }

    /**
     * Permanently stops the Worker from executing anymore commands. Used when shutting down the application.
     */
    public void KillWorker() {
        this.workerThread.interrupt();
    }

}
