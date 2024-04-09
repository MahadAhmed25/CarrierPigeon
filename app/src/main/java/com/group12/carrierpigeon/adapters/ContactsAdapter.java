package com.group12.carrierpigeon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.carrierpigeon.R;
import com.group12.carrierpigeon.components.contacts.Contact;
import com.group12.carrierpigeon.components.contacts.ContactsViewHolder;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsViewHolder> {

    Context context;
    List<Contact> contacts;

    public ContactsAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.phoneNo.setText(contacts.get(position).getPhoneNo());
        holder.contactDP.setImageResource(contacts.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
