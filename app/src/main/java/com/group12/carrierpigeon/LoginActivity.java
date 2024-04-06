package com.group12.carrierpigeon;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group12.carrierpigeon.controller.Authentication;
import com.group12.carrierpigeon.threading.Subscriber;

public class LoginActivity extends AppCompatActivity implements Subscriber<Boolean> {

    private static Authentication authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // When the login button is pressed, this method will fire
    public void onLoginClick(View view) {
        try {
            // To find items from the view (i.e., objects within the xml file), call this method along with the identifier of the object
            TextView status = this.findViewById(R.id.status);
            CharSequence text = "Communicating with authentication services...";
            status.setText(text);

            // Get username and password
            EditText username = this.findViewById(R.id.usernameText);
            EditText password = this.findViewById(R.id.passwordText);

            if (authController == null) {
                authController = new Authentication("192.168.2.56",1250);
            }

            authController.subscribe(this);
            authController.init(username.getText().toString(),password.getText().toString());

        } catch (Exception e) {
        }
    }

    @Override
    public void update(Boolean context) {
        TextView status = this.findViewById(R.id.status);
        if (context) {
            CharSequence valid = "Validated user credentials, please wait...";
            status.setText(valid);
        } else {
            CharSequence invalid = "Unable to authenticate user";
            status.setText(invalid);
        }
    }
}