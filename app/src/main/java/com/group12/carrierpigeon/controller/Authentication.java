package com.group12.carrierpigeon.controller;

import com.group12.carrierpigeon.networking.DataObject;
import com.group12.carrierpigeon.threading.Command;
import com.group12.carrierpigeon.threading.Publisher;
import com.group12.carrierpigeon.threading.ReturnCommand;
import com.group12.carrierpigeon.threading.Subscriber;
import com.group12.carrierpigeon.threading.Worker;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Authentication Controller class responsible for session tickets and their validity
 */
public class Authentication extends Publisher<Boolean> implements Subscriber<DataObject> {
    private String authServerIp;
    private Integer authServerPort;
    private String username;
    private String password;
    private byte[] ticket = null;
    private Worker<DataObject> authServerComWorker;
    private Socket comSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ReturnCommand<DataObject> connectToAuthServer = () -> {
        try {
            // Attempt new connection
            this.comSocket = new Socket(this.authServerIp,this.authServerPort);
            this.out = new ObjectOutputStream(this.comSocket.getOutputStream());
            this.in = new ObjectInputStream(this.comSocket.getInputStream());
            // Get ticket
            // Send request
            out.writeObject(new DataObject(DataObject.Status.VALID,("AUTH:"+username+":"+password).getBytes(StandardCharsets.UTF_8)));
            // Await response
            DataObject ticket = (DataObject) in.readObject();
            if (!ticket.getStatus().equals(DataObject.Status.FAIL)) {
                // If valid DataObject, ticket should have been response, thus, store it
                this.ticket = ticket.getData();
            }
            return new DataObject(DataObject.Status.VALID,null);
        } catch (Exception ignore) {
            return new DataObject(DataObject.Status.FAIL,null);
        }
    };

    private ReturnCommand<DataObject> checkTicket = () -> {
        try {
            // Before attempting check, check to see if socket is not closed
            // It's possible server disconnected connection due to inactivity
            // This will get the most up to date ticket if connection was previously closed
            this.checkConnection.performAction();
            // Send request to check ticket
            out.writeObject(new DataObject(DataObject.Status.VALID,("CHECK:"+new String(this.ticket)).getBytes(StandardCharsets.UTF_8)));
            // Await response
            return (DataObject) in.readObject();
        } catch (Exception ignored) {
            return new DataObject(DataObject.Status.FAIL,null);
        }
    };

    private Command checkConnection = () -> {
        try {
            if (this.comSocket != null && this.comSocket.isClosed()) {
                this.comSocket.close();
                this.out.close();
                this.in.close();
                // Reestablish connection
                this.connectToAuthServer.performAction();
            }
        } catch (Exception ignored) {};

    };

    public Authentication(String serverIp, Integer port) {
        this.authServerIp = serverIp;
        this.authServerPort = port;
        this.authServerComWorker = new Worker<>();
    }


    public void init(String username, String password) {
        this.username = username;
        this.password = password;
        // Give connection command to worker to run on separate thread
        this.authServerComWorker.addReturnCommand(connectToAuthServer).subscribe(this);
    }


    @Override
    public void update(DataObject context) {
        if (context.getStatus().equals(DataObject.Status.VALID)) {
            // When Authentication is notified, it is done so via the UI thread
            // Any logic to be executed by subscribers of the controller is done via the UI thread too
            this.notifySubscribersInSameThread(true);
        } else {
            this.notifySubscribersInSameThread(false);
        }
        this.unsubscribeAll();
    }
}
