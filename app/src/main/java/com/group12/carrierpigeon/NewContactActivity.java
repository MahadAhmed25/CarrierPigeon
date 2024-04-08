package com.group12.carrierpigeon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.carrierpigeon.components.contacts.Contact;
import com.group12.carrierpigeon.components.contacts.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewContactActivity extends AppCompatActivity {

    ImageView backArrow;
    Toolbar toolbar;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_newcontact);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.add_new_contact);

        this.backArrow = toolbar.findViewById(R.id.back_arrow_icon);
        backArrow.setOnClickListener(v -> finish());


    }
}
