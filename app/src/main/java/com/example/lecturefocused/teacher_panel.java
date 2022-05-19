package com.example.lecturefocused;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class teacher_panel extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    ImageView image,image1;
    TextView txt1,txt2,txt3,txt4;
    String userid;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_panel);
        image = findViewById(R.id.teacher_panel_image);
        image1 = findViewById(R.id.teacher_panel_options);
        txt1 = findViewById(R.id.name_teacher_panel);
        txt2 = findViewById(R.id.id_teacher_panel);
        txt3 = findViewById(R.id.department_teacher_panel);
        txt4 = findViewById(R.id.desig_teacher_panel);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userid = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                txt1.setText(documentSnapshot.getString("Name"));
                txt2.setText(documentSnapshot.getString("ID"));
                txt3.setText(documentSnapshot.getString("Department"));
                txt4.setText(documentSnapshot.getString("Class"));
            }
        });


        StorageReference profileref = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(image);
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(teacher_panel.this);
                builder.setTitle("Options Menu")
                        .setMessage("Currently this is only available for logging out.\nDo you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(teacher_panel.this,"Logged Out",Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(teacher_panel.this,"Keeping logged in.",Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();
            }
        });

        cardView = findViewById(R.id.mobile_deliver);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(teacher_panel.this, deliver_lecture.class);

                Log.e("testing ++++++++++", "onClick: next Activity");
                startActivity(intent);
            }
        });

    }
}
