package com.group12.carrierpigeon.controller;

import com.group12.carrierpigeon.components.accounts.Account;
import com.group12.carrierpigeon.networking.DataObject;
import com.group12.carrierpigeon.threading.Publisher;
import com.group12.carrierpigeon.threading.Subscriber;
import com.group12.carrierpigeon.threading.Worker;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class ChatManagement extends Publisher<List<Object>> implements Subscriber<List<Object>> {

    private Encryption encryptionController;
    private Authentication authenticationController;
    private List<String> messageDetails;
    private boolean gettingMessages = false;

    private Worker<List<Object>> chatWorker;

    public ChatManagement(Encryption encryptionController, Authentication authenticationController) {
        this.encryptionController = encryptionController;
        this.authenticationController = authenticationController;
        this.encryptionController.subscribe(this);
        this.chatWorker = new Worker<>();
    }

    /**
     * Sends a message to a user. Before invoking any other methods, wait until "SENT" has been called as whoIs in update()
     * @param message the given plaintext message to send
     * @param username the given username to send the message from
     * @param recipient the given username of the recipient who will receive the message
     */
    public void sendMessage(String message, String username, String recipient) {
        this.gettingMessages = false;
        // Add message details to memory (because ChatManagement has to wait on getting encryption details)
        this.messageDetails = new ArrayList<String>() {{
            add(message);
            add(username);
            add(recipient);
        }};
        // Check if a key and iv already exist in the backend (indicates that a chat between the two users exists)
        // This also checks if the ticket is valid
        this.encryptionController.getEncryptionDetails(username,recipient);
    }

    /**
     * Gets messages from a given user received by another user
     * @param sentUser the username of the user who was sending messages
     * @param toUser the username of the user who was receiving the messages (i.e., this method will get the messages they have received)
     */
    public void getMessages(String sentUser, String toUser) {
        this.gettingMessages = true;
        this.messageDetails = new ArrayList<String>() {{
            add(sentUser);
            add(toUser);
        }};
        this.encryptionController.getEncryptionDetails(sentUser,toUser);
    }

    private void createChat() {
        Account account = this.authenticationController.getAccount();
        this.chatWorker.addReturnCommand(() -> {
            try {
                // Ask to create new chat
                account.getOut().writeObject(new DataObject(DataObject.Status.VALID,("NEWCHAT:"+this.messageDetails.get(1)+":"+this.messageDetails.get(2)+":"+new String(this.authenticationController.getAccount().getTicket())).getBytes(StandardCharsets.UTF_8)));
                // The return is a DataObject which contains both the key and iv
                DataObject response = (DataObject) account.getIn().readObject();
                return Encryption.extractEncryptionInfo(response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        },"MESG").subscribe(this);
    }

    private void sendMessage(List<Object> encryptionDetails) {
        Account account = this.authenticationController.getAccount();
        this.chatWorker.addReturnCommand(() -> {
            try {
                // Encrypt message
                String encryptedMessage = Encryption.encrypt(this.messageDetails.get(0),(SecretKey) encryptionDetails.get(0),(IvParameterSpec) encryptionDetails.get(1));
                account.getOut().writeObject(new DataObject(DataObject.Status.VALID,("MSG:"+this.messageDetails.get(1)+":"+this.messageDetails.get(2)+":"+encryptedMessage+":"+new String(account.getTicket())).getBytes(StandardCharsets.UTF_8)));
                DataObject response = (DataObject) account.getIn().readObject();
                if (response.getStatus().equals(DataObject.Status.VALID)) {
                    return new ArrayList<Object>() {{
                        add("VALID");
                    }};
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<Object>() {{
                    add("FAIL");
                }};
            }
            return new ArrayList<Object>() {{
                add("FAIL");
            }};
        },"DONE").subscribe(this);
    }

    private void getMessages(List<Object> encryptionDetails) {
        Account account = this.authenticationController.getAccount();
        this.chatWorker.addReturnCommand(() -> {
            try {
                // Ask to create new chat
                account.getOut().writeObject(new DataObject(DataObject.Status.VALID,("GETMSG:"+this.messageDetails.get(0)+":"+this.messageDetails.get(1)+":"+new String(account.getTicket())).getBytes(StandardCharsets.UTF_8)));
                // The return is a DataObject contains the latest ten messages
                DataObject response = (DataObject) account.getIn().readObject();
                if (response.getStatus().equals(DataObject.Status.VALID)) {
                    List<Object> messages = new ArrayList<>();
                    // Split all messages
                    String[] msgs = new String(response.getData()).split(",");
                    for (String msg : msgs) {
                        // Decrypt each message
                        messages.add(Encryption.decrypt(msg,(SecretKey) encryptionDetails.get(0), (IvParameterSpec) encryptionDetails.get(1)));
                        System.out.println(messages.get(0));
                    }
                    return messages;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        },"REC").subscribe(this);
    }

    @Override
    public void update(List<Object> context, String whoIs) {
        if (whoIs != null && whoIs.contains("ENCINFO-FAIL") && !this.gettingMessages) {
            // If failed, create a new chat
            this.createChat();
        } else if (whoIs != null && whoIs.contains("ENCINFO") && !this.gettingMessages) {
            // If not failed, send a message as normal
            this.sendMessage(context);
        } else if (whoIs != null && whoIs.contains("MESG") && context != null) {
            // Once encryption info is created, then call send message
            this.sendMessage(context);
        } else if (whoIs != null && whoIs.contains("DONE") && context != null) {
            this.notifySubscribersInSameThread(new ArrayList<Object>(){{add("SENT!");}},"SENT");
        } else if (whoIs != null && whoIs.contains("ENCINFO") && this.gettingMessages) {
            // Call to get messages
            this.getMessages(context);
        } else if (whoIs != null && whoIs.contains("REC") && context != null) {
            // Return messages
            this.notifySubscribersInSameThread(context,"MESSAGES");
        }
    }
}
