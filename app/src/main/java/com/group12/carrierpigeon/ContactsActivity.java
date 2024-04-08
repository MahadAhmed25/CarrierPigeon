package com.group12.carrierpigeon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        RecyclerView recyclerView = findViewById(R.id.contact_recycler);

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John Wick", "647 706 4803", R.drawable.a ));
        contacts.add(new Contact("Mahad Ahmed", "123 493 3312", R.drawable.a ));
        contacts.add(new Contact("John Doe", "647 321 3543", R.drawable.a ));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ContactsAdapter(getApplicationContext(), contacts ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contactscreen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.new_contact){
            Intent move = new Intent(this, NewContactActivity.class);
            startActivity(move);
        } else if (item.getItemId() == R.id.logout) {
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "what", Toast.LENGTH_SHORT).show();
        }
        return true;

    }
}
