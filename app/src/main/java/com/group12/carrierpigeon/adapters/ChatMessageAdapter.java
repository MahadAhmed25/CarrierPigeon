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

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatModelViewHolder> {

    Context context;
    List<ChatMessageViewHolder> messagesList;

    public ChatMessageAdapter(Context context, List<ChatMessageViewHolder> messagesList){
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("You have reached this 0");
        View view = LayoutInflater.from(context).inflate(R.layout.chatmessage_recycler, parent, false);
        return new ChatModelViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


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
