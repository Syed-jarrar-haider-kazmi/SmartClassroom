package com.example.lecturefocused;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.MyviewHolder> {

    ArrayList<String> data1 = new ArrayList<String>();
    ArrayList<String> data2 = new ArrayList<String>();
    ArrayList<String> data = new ArrayList<String>();
    ArrayList<String> jtimes = new ArrayList<String>();
    ArrayList<String> ltimes = new ArrayList<String>();

    int pic;

    MyContactAdapter(ArrayList<String> data, ArrayList<String> data1, ArrayList<String> data2, ArrayList<String> jtimes, ArrayList<String> ltimes, int pic) {
        this.data = data;
        this.data1 = data1;
        this.data2 = data2;
        this.jtimes = jtimes;
        this.ltimes = ltimes;
        this.pic = pic;


    }

    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_list_layout, parent, false);

        return new MyviewHolder(view);
    }

    String con, no, p;

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {
        con = data.get(position);
//        no = data1.get(position);
//        p = data2.get(position);
//        Log.d("Dignose", "onBindViewHolder: ================================ " + data1.get(position));
//        Log.d("Dignose", "onBindViewHolder: ================================ " + data2.get(position));
      //  holder.contact.setText(con+"\t\t"+data2.get(position)+"\n\n"+no);
        holder.contact.setText(con);
//        holder.seatNo.setText(no);
//        holder.present.setText(p);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        ImageView imgcontact;
        TextView contact, seatNo, present;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            imgcontact = itemView.findViewById(R.id.ImgContact);
            contact = itemView.findViewById(R.id.contacts);
            seatNo = itemView.findViewById(R.id.myseat);
            present = itemView.findViewById(R.id.attendence);

            //implementing on click listner
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int a = getAdapterPosition();// getting the position of the selected contact
                    Intent ints = new Intent(v.getContext(), result_stat.class);

                    ints.putExtra("ssnames", data.get(a));  // sending the particular data of contact
                    ints.putExtra("sseat", data1.get(a));

                    ints.putExtra("JoinT", jtimes.get(a));  // sending the particular data of contact
                    ints.putExtra("LeaveT", ltimes.get(a));
                    ints.putExtra("present", "P");

                    v.getContext().startActivity(ints);

                }
            });

        }
    }
}




