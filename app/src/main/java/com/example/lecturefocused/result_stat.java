package com.example.lecturefocused;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lecturefocused.deliver_lecture;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class result_stat extends AppCompatActivity {

    TextView snames, seat, status, ctime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_stat);

        status = (TextView) findViewById(R.id.Attstatus);
        seat = (TextView) findViewById(R.id.sseatno);
        snames = (TextView) findViewById(R.id.sstudentname);
        ctime = (TextView) findViewById(R.id.connectstime);


        Intent intent = getIntent();
        String time1=intent.getStringExtra("JoinT");
       String time2=intent.getStringExtra("LeaveT");


        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date d1 = null,d2=null;
        try {
             d1 = formatter.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
             d2 = formatter.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeDiff = d2.getTime() - d1.getTime();
        Date diff = new Date(timeDiff);
        status.setText("Attendance Status : " + intent.getStringExtra("present"));
        seat.setText("Seat: "+intent.getStringExtra("sseat"));
        snames.setText("Name : "+intent.getStringExtra("ssnames"));
        ctime.setText("Duration : "+ (timeDiff / 3600000) + " hrs " + (timeDiff % 3600000) / 60000 + " min "+ timeDiff/1000 +" s");

    }
}