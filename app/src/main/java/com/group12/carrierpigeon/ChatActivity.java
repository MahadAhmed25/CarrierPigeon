package com.group12.carrierpigeon;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.carrierpigeon.adapters.ChatMessageAdapter;
import com.group12.carrierpigeon.adapters.ContactsAdapter;
import com.group12.carrierpigeon.components.chat.ChatMessageViewHolder;
import com.group12.carrierpigeon.controller.Authentication;
import com.group12.carrierpigeon.controller.ChatManagement;
import com.group12.carrierpigeon.controller.Encryption;
import com.group12.carrierpigeon.threading.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements Subscriber<List<Object>> {

    private Encryption encryptionController;
    private ChatManagement chatManagementController;
    private Authentication authController;
    private String contact;

    private List<ChatMessageViewHolder> messages;
    private int setup = 0;

    ImageButton backBtn;
    EditText messageInput;
    TextView otherUser;
    RecyclerView recyclerView;
    ImageButton sendMessagebtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        backBtn = findViewById(R.id.back_arrow_icon);
        backBtn.setOnClickListener(v -> finish());

        messageInput = findViewById(R.id.message_send_input);
        otherUser = findViewById(R.id.other_user);
        otherUser.setText(getIntent().getExtras().getString("user"));

        recyclerView = findViewById(R.id.chatroom_recylcer);
        sendMessagebtn = findViewById(R.id.message_send_btn);

        this.messages = new ArrayList<>();

        this.setup();

    }

    public void onSendButtonPressed(View view) {
        // Get message that was typed by user
        messageInput.setEnabled(false);
        sendMessagebtn.setEnabled(false);
        String message = messageInput.getText().toString();
        // Call chat controller to send message
        chatManagementController.sendMessage(message,Info.username,this.contact);
    }

    private void setup() {
        this.contact = getIntent().getExtras().getString("user");
        this.authController = LoginActivity.authController;
        encryptionController = new Encryption(this.authController);
        chatManagementController = new ChatManagement(this.encryptionController,this.authController);
        // Chat Controller will get messages on a separate thread, thus need to subscribe to it for results
        this.chatManagementController.subscribe(this);
        // Get messages sent by contact to person
        this.chatManagementController.getMessages(this.contact,Info.username);
    }

    @Override
    public void update(List<Object> context, String whoIs) {
        if (whoIs != null && whoIs.contains("MESSAGES") && this.setup == 0) {
            for (Object msg : context) {
                String mesg = (String) msg;
                if (!mesg.isEmpty()) {
                    this.messages.add(new ChatMessageViewHolder(this.contact,(String) msg));
                }
            }
            this.setup = 1;
            // Get messages sent by user to contact
            this.chatManagementController.getMessages(Info.username,this.contact);
        } else if (whoIs != null && whoIs.contains("MESSAGES") && this.setup == 1) {
            for (Object msg : context) {
                String mesg = (String) msg;
                if (!mesg.isEmpty()) {
                    this.messages.add(new ChatMessageViewHolder(Info.username, (String) msg));
                }
            }
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ChatMessageAdapter adapter = new ChatMessageAdapter(getApplicationContext(), messages);
            this.recyclerView.setAdapter(adapter);
        }
        else if (whoIs != null && whoIs.contains("SENT") && context != null) {
            // Check if the message was sent successfully
            if (((String) context.get(0)).contains("VALID")) {
                messageInput.setEnabled(true);
                // Clearing text indicates it sent
                messageInput.getText().clear();
                sendMessagebtn.setEnabled(true);
                // Indicates the chat needs to be updated
                this.messages.clear();
                this.setup = 0;
                // Request to get messages again
                this.chatManagementController.getMessages(this.contact,Info.username);
            } else {
                // Message sending was not successful so just re-enable buttons
                messageInput.setEnabled(true);
                sendMessagebtn.setEnabled(true);
            }
        }
    }
}
