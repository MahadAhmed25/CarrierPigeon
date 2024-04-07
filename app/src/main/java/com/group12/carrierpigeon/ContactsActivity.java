package com.group12.carrierpigeon;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.carrierpigeon.components.contacts.Contact;
import com.group12.carrierpigeon.components.contacts.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);

        RecyclerView recyclerView = findViewById(R.id.contact_recycler);

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John Wick", "647 706 4803", R.drawable.a ));
        contacts.add(new Contact("Mahad Ahmed", "123 493 3312", R.drawable.a ));
        contacts.add(new Contact("John Doe", "647 321 3543", R.drawable.a ));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ContactsAdapter(getApplicationContext(), contacts ));
    }
}
