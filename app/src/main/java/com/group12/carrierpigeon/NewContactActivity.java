package com.group12.carrierpigeon;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewContactActivity extends AppCompatActivity {

    ImageView backArrow;
    Toolbar toolbar;
    TextView toolbarTitle;
    EditText phoneNum;

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

        this.phoneNum = this.findViewById(R.id.phoneno_editText);

    }

    // When the search button is pressed, this method will fire
    public void onSearchClick(View view) {

    }
}
