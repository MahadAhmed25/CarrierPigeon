package com.group12.carrierpigeon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.carrierpigeon.components.accounts.Account;
import com.group12.carrierpigeon.components.contacts.Contact;
import com.group12.carrierpigeon.adapters.ContactsAdapter;
import com.group12.carrierpigeon.controller.Authentication;
import com.group12.carrierpigeon.networking.DataObject;
import com.group12.carrierpigeon.threading.Subscriber;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements Subscriber<DataObject> {

    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);



        this.contacts = new ArrayList<>();

        //Authentication authController = new Authentication("70.49.90.188",1250);
        Account account = LoginActivity.authController.getAccount();
        account.setCredentials(Info.username, Info.password);
        LoginActivity.authController.authenticate(Info.username, Info.password);
        account.subscribe(this);
        LoginActivity.authController.getAccount().getContacts();

    }

    @Override
    public void update(DataObject context, String whoIs) {
        if (context.getData() != null && whoIs.equals("CONTACTS")) {
            String dataString = new String(context.getData(), StandardCharsets.UTF_8);
            System.out.println(dataString);
            populateContacts(dataString);

            RecyclerView recyclerView = findViewById(R.id.contact_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ContactsAdapter(getApplicationContext(), contacts));
        }
    }

    public void populateContacts(String usersString) {
        String[] usersArray = usersString.split(",");
        for (String user: usersArray) {
            if (!user.isEmpty()) {
                this.contacts.add(new Contact(user.trim(), "1 111 111 1111", R.drawable.a));
            }
        }
        System.out.println(contacts + "bye");
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
