package com.example.lecturefocused;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class AttendenceList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_list);


        RecyclerView ContactList=(RecyclerView) findViewById(R.id.contacts);


        Intent i =getIntent();



        ArrayList<String> student=new ArrayList<String>();
        ArrayList<String>Seatno=new ArrayList<String>();
        ArrayList<String> Attendence=new ArrayList<String>();

        ArrayList<String>jtimes=new ArrayList<String>();
        ArrayList<String> ltimes=new ArrayList<String>();

         student=i.getStringArrayListExtra("Snames");
         Seatno=i.getStringArrayListExtra("SeatNo");
         jtimes=i.getStringArrayListExtra("jtime");
        ltimes=i.getStringArrayListExtra("ltime");
        ContactList.setLayoutManager(new LinearLayoutManager(this));


        //student.add("Syed Kazmi");
        Attendence.add("P");
//        Seatno.add("CS-203");
//        student.add("Saif");
//        Attendence.add("P");

        int images=R.drawable.contact_pic;
        MyContactAdapter a= new MyContactAdapter(student,Seatno,Attendence,jtimes,ltimes,images);

        ContactList .setAdapter(new MyContactAdapter(student,Seatno,Attendence,jtimes,ltimes,images));



    }
}