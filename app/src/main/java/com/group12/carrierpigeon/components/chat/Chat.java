package com.group12.carrierpigeon.components.chat;

import com.group12.carrierpigeon.components.Source;
import com.group12.carrierpigeon.components.accounts.Account;
import com.group12.carrierpigeon.networking.DataObject;
import com.group12.carrierpigeon.threading.Command;
import com.group12.carrierpigeon.threading.ReturnCommand;
import com.group12.carrierpigeon.threading.Worker;

import java.nio.charset.StandardCharsets;

public class Chat extends Source {
    Account account;
    private Worker<DataObject> worker;

    public Chat(Account account) {
        this.account = account;
        this.worker = new Worker<>();
    }

    public void sendMessage(String message) {
        this.worker.addReturnCommand(() -> {
            try {
                // Testing writing a message from testUser to testUser1
                account.getOut().writeObject(new DataObject(DataObject.Status.VALID,("MSG:testUser:testUser1:"+message+":"+account.getTicket()).getBytes(StandardCharsets.UTF_8)));
                DataObject object = (DataObject) account.getIn().readObject();
                if (object.getStatus().equals(DataObject.Status.VALID)) {
                    return new DataObject(DataObject.Status.VALID,null);
                }
            } catch (Exception e) {
            }
            return new DataObject(DataObject.Status.FAIL,null);
        }).subscribe(this);
    }


    @Override
    public void handleDataCommand(Command data, String whoIs) {

    }

    @Override
    public void handleDataResponseCommand(ReturnCommand<DataObject> data, String whoIs) {

    }

    @Override
    public void update(DataObject context, String whoIs) {
    }
}
