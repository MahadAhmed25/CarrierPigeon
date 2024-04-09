package com.group12.carrierpigeon.components.contacts;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.carrierpigeon.R;


public class ContactsViewHolder extends RecyclerView.ViewHolder {

    public ImageView contactDP;
    public TextView name;
    public TextView phoneNo;

    public ContactsViewHolder(@NonNull View itemView) {
        super(itemView);
        contactDP = itemView.findViewById(R.id.contactdp);
        name = itemView.findViewById(R.id.name);
        phoneNo = itemView.findViewById(R.id.phoneno);
    }
}
