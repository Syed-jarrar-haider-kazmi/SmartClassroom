package com.example.lecturefocused.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.example.lecturefocused.FingerPrint;
import com.example.lecturefocused.R;
import com.example.lecturefocused.Scanner;
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

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    ImageView image;
    TextView txt1,txt2,txt3,txt4;
    String userid;
    CardView cardView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        image = (ImageView)root.findViewById(R.id.image_student_panel);
        txt1 = (TextView)root.findViewById(R.id.name_student_panel);
        txt2 = (TextView)root.findViewById(R.id.rollno_student_panel);
        txt3 = (TextView)root.findViewById(R.id.department_student_panel);
        txt4 = (TextView)root.findViewById(R.id.class_student_panel);

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

        cardView = root.findViewById(R.id.mobile);



        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FingerPrint.class);

                startActivity(intent);


//                Intent intent = new Intent(getActivity(), attend_lecture.class);
//                startActivity(intent);
            }
        });



        return root;
    }
}
