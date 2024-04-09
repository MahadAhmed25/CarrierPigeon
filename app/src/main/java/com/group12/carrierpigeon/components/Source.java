package com.group12.carrierpigeon.components;

import com.group12.carrierpigeon.networking.DataObject;
import com.group12.carrierpigeon.threading.Command;
import com.group12.carrierpigeon.threading.Publisher;
import com.group12.carrierpigeon.threading.ReturnCommand;
import com.group12.carrierpigeon.threading.Subscriber;

public abstract class Source extends Publisher<DataObject> implements Subscriber<DataObject> {

    public abstract void handleDataCommand(Command data, String whoIs);

    public abstract void handleDataResponseCommand(ReturnCommand<DataObject> data, String whoIs);

}
