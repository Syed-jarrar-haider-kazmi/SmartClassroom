package com.example.lecturefocused;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class logout extends AppCompatActivity {
    Button b1, b2;
    boolean logout = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        b1 = findViewById(R.id.logout_yes);
        b2 = findViewById(R.id.logout_No);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                //System.exit(0);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(logout.this, student_panel.class);
                startActivity(I);
                finish();
            }
        });
    }
}
