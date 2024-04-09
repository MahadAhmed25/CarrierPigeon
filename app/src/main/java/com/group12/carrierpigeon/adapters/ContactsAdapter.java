package com.group12.carrierpigeon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.carrierpigeon.ChatActivity;
import com.group12.carrierpigeon.R;
import com.group12.carrierpigeon.components.contacts.Contact;
import com.group12.carrierpigeon.components.contacts.ContactsViewHolder;

import java.util.List;

/**
 * Adapter for displaying contacts in a RecyclerView.
 * Binds contact data to ContactsViewHolder and handles click events to start chat activity.
 * Adapter was referenced from here: https://www.youtube.com/watch?v=TAEbP_ccjsk
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsViewHolder> {

    Context context;
    List<Contact> contacts;

    /**
     * Creates a ContactsAdapter instance.
     * @param context The context in which the adapter will be used.
     * @param contacts The list of contacts to be displayed.
     */
    public ContactsAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    /**
     * Called when RecyclerView needs a new ContactsViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ContactsViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_view, parent, false));
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ContactsViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.phoneNo.setText(contacts.get(position).getPhoneNo());
        holder.contactDP.setImageResource(contacts.get(position).getImage());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("user", contacts.get(position).getName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
