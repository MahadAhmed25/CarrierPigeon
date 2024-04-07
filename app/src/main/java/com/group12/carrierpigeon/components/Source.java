package com.group12.carrierpigeon.components;

import com.group12.carrierpigeon.components.Data.Data;

public abstract class Source {

    public abstract void handleData(Data data);

    public abstract void getData(Data data);

}
