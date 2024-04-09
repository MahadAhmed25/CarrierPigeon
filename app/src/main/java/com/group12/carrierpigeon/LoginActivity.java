package com.group12.carrierpigeon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group12.carrierpigeon.components.accounts.Account;
import com.group12.carrierpigeon.components.chat.Chat;
import com.group12.carrierpigeon.controller.Authentication;
import com.group12.carrierpigeon.threading.Subscriber;

public class LoginActivity extends AppCompatActivity implements Subscriber<Boolean> {

    private static Authentication authController;

    private Button login;
    private ProgressBar bar;
    private EditText username;
    private EditText password;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("This is an on create");
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

    // When the login button is pressed, this method will fire
    public void onLoginClick(View view) {
        try {

            System.out.println("This is a test!");

           // this.displayStatusText("Communicating with authentication services...");

            // Get username and password
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();

            // Clear/disable buttons/text inputs
            username.getText().clear();
            password.getText().clear();

            this.disableLogin();

            //bar.setVisibility(View.VISIBLE);


            if (authController == null) {
                authController = new Authentication("192.168.2.56",1250);
            }

            authController.subscribe(this);
            authController.Authenticate(usernameText,passwordText);

        } catch (Exception e) {
        }
    }

    @Override
    public void update(Boolean context) {
        if (context) {
           // this.displayStatusText("Validated user credentials, please wait...");
            // To move to another screen, use Intents
            //Intent move = new Intent(this, MainActivity.class);
            System.out.println("Switched to some shit");
            //switch screens
            Intent move= new Intent(this, ContactsActivity.class);
            startActivity(move);
            //startActivity(move);
            // Davis: Commented out method sends a message to testUser1
            //new Chat(authController.getAccount()).sendMessage("Wow!");
        } else {
            //this.displayStatusText("Unable to authenticate user");
            //bar.setVisibility(View.INVISIBLE);
            System.out.println("This is very cool");
            this.enableLogin();
        }
    }

    //public void displayStatusText(String textToDisplay) {
       // status.setText(textToDisplay);
   // }

    public void disableLogin() {
        username.setEnabled(false);
        password.setEnabled(false);
        login.setEnabled(false);
    }

    public void enableLogin() {
        username.setEnabled(true);
        password.setEnabled(true);
        login.setEnabled(true);
    }

}