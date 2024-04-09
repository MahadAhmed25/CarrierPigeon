package com.group12.carrierpigeon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.group12.carrierpigeon.dialogs.LoadingDialog;
import com.group12.carrierpigeon.networking.DataObject;
import com.group12.carrierpigeon.threading.Subscriber;

/**
 * The NewContactActivity class handles the addition of new contacts to the user's contact list.
 */
public class NewContactActivity extends AppCompatActivity implements Subscriber<DataObject> {

    ImageView backArrow;
    Toolbar toolbar;
    TextView toolbarTitle;
    EditText usernameToAdd;
    LoadingDialog loadingDialog;

    /**
     * Initializes the activity layout and sets up necessary UI elements and event listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_newcontact);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarTitle = this.toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.add_new_contact);

        this.backArrow = this.toolbar.findViewById(R.id.back_arrow_icon);
        backArrow.setOnClickListener(v -> finish());

        this.usernameToAdd = this.findViewById(R.id.username_editText);

        this.loadingDialog = new LoadingDialog(this);
        // Need to subscribe as account will run methods in different thread
        LoginActivity.authController.getAccount().subscribe(this);

    }

    /**
     * Method called when the search button is clicked. Initiates the process of adding a new contact.
     *
     * @param view The view that triggered the click event (in this case, the search button).
     */
    public void onSearchClick(View view) {
        // Show loading screen
        this.loadingDialog.show();
        String username = usernameToAdd.getText().toString();
        // Call addContact to attempt to add new contact
        LoginActivity.authController.getAccount().addContact(username);

    }

    /**
     * Updates the UI based on the result of the new contact addition process.
     *
     * @param context The result of the new contact addition process.
     * @param whoIs
     */
    @Override
    public void update(DataObject context, String whoIs) {
        if (whoIs != null && whoIs.contains("ADDCONTACT")) {
            loadingDialog.cancel();
            if (context.getStatus().equals(DataObject.Status.VALID)) {
                // Contact was successfully added, thus switch back to main contact screen
                Intent move = new Intent(this, ContactsActivity.class);
                startActivity(move);
            }
        }
    }
}
