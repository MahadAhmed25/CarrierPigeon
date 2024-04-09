package com.group12.carrierpigeon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.carrierpigeon.Info;
import com.group12.carrierpigeon.R;
import com.group12.carrierpigeon.components.chat.ChatMessageViewHolder;

import java.util.List;

/**
 * Adapter for displaying chat messages in a RecyclerView.
 * Reference: https://www.youtube.com/watch?v=er-hKSt1r7E&list=PLgpnJydBcnPB-aQ6P5hWCHBjy8LWZ9x4w&index=15
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatModelViewHolder> {

    Context context;
    List<ChatMessageViewHolder> messagesList;

    /**
     * Constructor for the ChatMessageAdapter class.
     *
     * @param context      The context of the activity or fragment.
     * @param messagesList The list of chat message view holders to be displayed.
     */
    public ChatMessageAdapter(Context context, List<ChatMessageViewHolder> messagesList){
        this.context = context;
        this.messagesList = messagesList;
    }

    /**
     * Inflates the layout for each chat message item in the RecyclerView.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The type of the new View.
     * @returns A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("You have reached this 0");
        View view = LayoutInflater.from(context).inflate(R.layout.chatmessage_recycler, parent, false);
        return new ChatModelViewHolder(view);
    }

    /**
     * Binds data to the views in the ViewHolder.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position ) {
        ChatMessageViewHolder msg = messagesList.get(position);
        System.out.println(msg.getMessage() + " " + msg.getSender());

        if (msg.getSender().equals(Info.username)) {
            holder.receiverBubble.setVisibility(View.GONE);
            holder.senderBubble.setVisibility(View.VISIBLE);
            holder.senderMessage.setText(msg.getMessage());

        } else {
            holder.senderBubble.setVisibility(View.GONE);
            holder.receiverBubble.setVisibility(View.VISIBLE);
            holder.receiverMessage.setText(msg.getMessage());
        }

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @returns The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    /**
     * ViewHolder class to hold references to the views that will be populated with chat message data.
     */
    class ChatModelViewHolder extends RecyclerView.ViewHolder {
        LinearLayout senderBubble, receiverBubble;
        TextView senderMessage, receiverMessage;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            senderBubble = itemView.findViewById(R.id.sender_chat_bubble);
            receiverBubble = itemView.findViewById(R.id.receiver_chat_bubble);
            senderMessage = itemView.findViewById(R.id.sender_textMessage);
            receiverMessage = itemView.findViewById(R.id.receiver_textMessage);
        }
    }
}
