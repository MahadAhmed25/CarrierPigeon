package com.group12.carrierpigeon.components.accounts;

import com.group12.carrierpigeon.components.Source;
import com.group12.carrierpigeon.networking.DataObject;
import com.group12.carrierpigeon.threading.Command;
import com.group12.carrierpigeon.threading.ReturnCommand;
import com.group12.carrierpigeon.threading.Worker;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Account extends Source {
    private byte[] ticket;
    private String username;
    private String password;
    private Worker<DataObject> accountWorker;
    private String serverIp;
    private Integer serverPort;
    private Socket comSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ReturnCommand<DataObject> checkTicket = () -> {
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

    public Command checkConnection = () -> {
        try {
            if (this.comSocket != null && this.comSocket.isClosed()) {
                this.comSocket.close();
                this.out.close();
                this.in.close();
                // Reestablish connection
                this.init.performAction();
            }
        } catch (Exception ignored) {};

    };

    public ReturnCommand<DataObject> init = () -> {
        try {
            if (this.comSocket == null || this.comSocket.isClosed() || !this.comSocket.isConnected()) {
                // Attempt new connection
                if (this.comSocket != null) {
                    // Close any streams if possible
                    this.comSocket.close();
                }
                this.comSocket = new Socket();
                SocketAddress address = new InetSocketAddress(this.serverIp,this.serverPort);
                this.comSocket.connect(address,15000);
                this.out = new ObjectOutputStream(this.comSocket.getOutputStream());
                this.in = new ObjectInputStream(this.comSocket.getInputStream());
            }

            // Get ticket
            // Send request
            out.writeObject(new DataObject(DataObject.Status.VALID,("AUTH:"+username+":"+password).getBytes(StandardCharsets.UTF_8)));
            // Await response
            DataObject ticket = (DataObject) in.readObject();
            if (!ticket.getStatus().equals(DataObject.Status.FAIL)) {
                // If valid DataObject, ticket should have been response, thus, store it
                this.ticket = ticket.getData();
            } else {
                return new DataObject(DataObject.Status.FAIL,null);
            }
            return new DataObject(DataObject.Status.VALID,null);
        } catch (SocketException ignore) {
            // Thrown if socket fails to connect to server in specified timeout period
            return new DataObject(DataObject.Status.FAIL,null);
        } catch (Exception ignore) {
            return new DataObject(DataObject.Status.FAIL,null);
        }
    };

    public Account () {
        this.username = null;
        this.password = null;
        ticket = null;
        this.accountWorker = new Worker<>();
    }

    public void setConnectionDetails(String serverIp, Integer serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Socket getComSocket() {
        return this.comSocket;
    }

    public ObjectInputStream getIn() {
        return this.in;
    }

    public ObjectOutputStream getOut() {
        return this.out;
    }

    public byte[] getTicket() {
        return this.ticket;
    }

    @Override
    public void handleDataCommand(Command data) {
        this.accountWorker.addCommand(data);
    }

    @Override
    public void handleDataResponseCommand(ReturnCommand<DataObject> data) {
        this.accountWorker.addReturnCommand(data).subscribe(this);
    }

    @Override
    public void update(DataObject context) {
        this.notifySubscribersInSameThread(context);
    }
}
