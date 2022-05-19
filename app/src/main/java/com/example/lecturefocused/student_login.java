package com.example.lecturefocused;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class student_login extends AppCompatActivity {
    EditText edittext1,edittext2;
    ImageView image;
    Button button;
    FirebaseAuth firebase;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        progressbar = findViewById(R.id.student_progressBar);
        progressbar.setVisibility(View.INVISIBLE);
        edittext1 = findViewById(R.id.student_enter_email);
        edittext2 = findViewById(R.id.student_enter_password);
        button = findViewById(R.id.student_login_button);
        image = findViewById(R.id.studentback_button);
        firebase = FirebaseAuth.getInstance();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(student_login.this, student_panel.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else
            {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(student_login.this,student_panel.class);
                    //startActivity(intent);
                    String email = edittext1.getText().toString().trim();
                    String pass = edittext2.getText().toString().trim();
                    if(TextUtils.isEmpty(email))
                    {
                        edittext1.setError("Email is required");
                        return;
                    }
                    if(TextUtils.isEmpty(pass))
                    {
                        edittext2.setError("Password is required");
                        return;
                    }
                    if(pass.length()<6)
                    {
                        edittext2.setError("Password must be >= 6 characters");
                        return;
                    }
                    progressbar.setVisibility(View.VISIBLE);

                    firebase.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(student_login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(student_login.this,student_panel.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(student_login.this,"Login not Successful"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }


                        }
                    });


                }
            });
        }



    }
}
