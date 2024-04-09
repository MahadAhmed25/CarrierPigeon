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

/**
 * Activity for displaying the list of contacts.
 */
public class ContactsActivity extends AppCompatActivity implements Subscriber<DataObject> {

    private List<Contact> contacts;

    /**
     * Initialize the activity and set up the UI components.
     */
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

    /**
     * Updates the UI with data received from the server.
     *
     * @param context The data received from the server.
     * @param whoIs   Indicates the type of data received.
     */
    @Override
    public void update(DataObject context, String whoIs) {
        if (context.getData() != null && whoIs.equals("CONTACTS")) {
            String dataString = new String(context.getData(), StandardCharsets.UTF_8);
            populateContacts(dataString);

            RecyclerView recyclerView = findViewById(R.id.contact_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ContactsAdapter(getApplicationContext(), contacts));
        }
    }

    /**
     * Populates the list of contacts based on the data received from the server.
     *
     * @param usersString The string containing the list of contacts.
     */
    public void populateContacts(String usersString) {
        String[] usersArray = usersString.split(",");
        for (String user: usersArray) {
            if (!user.isEmpty()) {
                this.contacts.add(new Contact(user.trim(), "1 647 432 3412", R.drawable.a));
            }
        }
        System.out.println(contacts + "bye");
    }

    /**
     * Inflates the menu to be displayed in the action bar.
     *
     * @param menu The menu to be inflated.
     * @returns True if the menu is successfully inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contactscreen_menu, menu);
        return true;
    }

    /**
     * Handles clicks on menu items.
     *
     * @param item The menu item that was clicked.
     * @returns True if the menu item click was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.new_contact){
            Intent move = new Intent(this, NewContactActivity.class);
            startActivity(move);
        } else if (item.getItemId() == R.id.logout) {
            // On logout, go back to login screen
            Intent move = new Intent(this, LoginActivity.class);
            startActivity(move);
        } else if (item.getItemId() == R.id.settings_btn) {
            Intent move = new Intent(this, SettingActivity.class);
            startActivity(move);
        } else if (item.getItemId() == R.id.help_support) {
            Toast.makeText(this, "redirecting to support page", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "what", Toast.LENGTH_SHORT).show();
        }
        return true;

    }



}
