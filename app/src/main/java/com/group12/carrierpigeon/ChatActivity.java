package com.group12.carrierpigeon;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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

    private List<String> sentMessages;
    private List<String> receivedMessages;
    private int setup = 0;

    ImageButton backBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        backBtn = findViewById(R.id.back_arrow_icon);
        backBtn.setOnClickListener(v -> finish());

        this.setup();

    }

    private void setup() {
        this.contact = getIntent().getExtras().getString("user");
        this.authController = LoginActivity.authController;
        encryptionController = new Encryption(this.authController);
        chatManagementController = new ChatManagement(this.encryptionController,this.authController);
        // Chat Controller will get messages on a separate thread, thus need to subscribe to it for results
        this.chatManagementController.subscribe(this);
        this.receivedMessages = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        // Get messages sent by contact to person
        this.chatManagementController.getMessages(this.contact,Info.username);
    }

    @Override
    public void update(List<Object> context, String whoIs) {
        if (whoIs != null && whoIs.contains("MESSAGES") && this.setup == 0) {
            for (Object msg : context) {
                this.receivedMessages.add((String) msg);
            }
            this.setup = 1;
            // Get messages sent by user to contact
            this.chatManagementController.getMessages(Info.username,this.contact);
        } else if (whoIs != null && whoIs.contains("MESSAGES") && this.setup == 1) {
            for (Object msg : context) {
                this.sentMessages.add((String) msg);
            }
        }
    }
}
