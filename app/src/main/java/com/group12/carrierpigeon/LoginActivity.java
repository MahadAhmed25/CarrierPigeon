package com.group12.carrierpigeon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group12.carrierpigeon.controller.Authentication;
import com.group12.carrierpigeon.threading.Subscriber;

/**
 * The LoginActivity class is responsible for handling user authentication and login functionality.
 */
public class LoginActivity extends AppCompatActivity implements Subscriber<Boolean> {

    public static Authentication authController;

    private Button login;
    private ProgressBar bar;
    private EditText username;
    private EditText password;
    private TextView status;

    /**
     * Initializes the activity layout and sets up necessary UI elements and event listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        // To find items from the view (i.e., objects within the xml file), call this method along with the identifier of the object
        this.login = this.findViewById(R.id.loginButton);
        this.login.setOnClickListener(this::onLoginClick);
        //this.bar = this.findViewById(R.id.progressBar);
        this.username = this.findViewById(R.id.usernameText);
        this.password = this.findViewById(R.id.passwordText);
        this.status = this.findViewById(R.id.status);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Method called when the login button is clicked. Initiates the authentication process.
     *
     * @param view The view that triggered the click event (in this case, the login button).
     */
    public void onLoginClick(View view) {
        try {

            // Get username and password
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();

            // Clear/disable buttons/text inputs
            username.getText().clear();
            password.getText().clear();

            this.disableLogin();


            if (authController == null) {
                authController = new Authentication(Info.IP,1250);
            }

            authController.subscribe(this);
            authController.authenticate(usernameText,passwordText);

        } catch (Exception e) {
        }
    }

    /**
     * Updates the UI based on the authentication result received from the authentication controller.
     *
     * @param context The result of the authentication process.
     * @param whoIs
     */
    @Override
    public void update(Boolean context, String whoIs) {
        if (context) {
            //switch screens
            Intent move= new Intent(this, ContactsActivity.class);
            startActivity(move);
        } else {
            Toast.makeText(this, "Unable to authenticate user", Toast.LENGTH_SHORT).show();
            this.enableLogin();
        }
    }

    /**
     * Helper method to disable login while authenticating
     */
    public void disableLogin() {
        username.setEnabled(false);
        password.setEnabled(false);
        login.setEnabled(false);
    }

    /**
     * Helper method to enable login after authenticating
     */
    public void enableLogin() {
        username.setEnabled(true);
        password.setEnabled(true);
        login.setEnabled(true);
    }

}