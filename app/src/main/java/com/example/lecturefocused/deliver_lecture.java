package com.example.lecturefocused;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class deliver_lecture extends AppCompatActivity {
    static final int SocketServerPORT = 8080;

    TextView infoIp, infoPort, chatMsg, Test;
    String msgLog = "";
    List<ChatClient> userList;
    ServerSocket serverSocket;
//StudentDetails mystudent=new StudentDetails();


    public List<ChatClient> getUserList() {
        return userList;
    }

    Button button ;

    ArrayList<String> Users;
    ArrayList<String> Onscreen_time;
    ArrayList<String>Joining_time=new ArrayList<String>();
    ArrayList<String>Leaving_time=new ArrayList<String>();
    ArrayList<String>SeatNo=new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_lecture);
        infoIp = (TextView) findViewById(R.id.infoip);
        infoPort = (TextView) findViewById(R.id.infoport);
        chatMsg = (TextView) findViewById(R.id.chatmsg);



        infoIp.setText(getIpAddress());

        userList = new ArrayList<ChatClient>();

        ChatServerThread chatServerThread = new ChatServerThread();
        chatServerThread.start();

        button = findViewById(R.id.end_lecture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveleaveTime();
              //  Intent intent = new Intent(deliver_lecture.this, result_stat.class);

                    Intent intent = new Intent(deliver_lecture.this, AttendenceList.class);
                    intent.putExtra("Snames", Users);
                    intent.putExtra("SeatNo", SeatNo);
                    intent.putExtra("jtime", Joining_time);
                    intent.putExtra("ltime", Leaving_time);
                    startActivity(intent);

            }
        });



    }

    public  void saveleaveTime() {
        if(Users!=null){
            for(int i=0;i<Users.size();i++){

               Leaving_time.add(getCurrentTime());
            }}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class ChatServerThread extends Thread {

        @Override
        public void run() {
            Socket socket = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                deliver_lecture.this.runOnUiThread(new Runnable() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        infoPort.setText("Port number : "
                                + serverSocket.getLocalPort());


                    }
                });

                while (true) {
                    socket = serverSocket.accept();
                    ChatClient client = new ChatClient();
                    userList.add(client);
                    ConnectThread connectThread = new ConnectThread(client, socket);
                    connectThread.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    public String getCurrentTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
        Date currentLocalTime = cal.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:mm:ss ");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));


return (date.format(currentLocalTime).toString()); // to get Value
    }


    private class ConnectThread extends Thread {

        Socket socket;
        ChatClient connectClient;

        String msgToSend = "";

        ConnectThread(ChatClient client, Socket socket){
            connectClient = client;
            this.socket= socket;
            client.socket = socket;
            client.chatThread = this;
        }

        @Override
        public void run() {
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {

                Users=new ArrayList<String>();
              Onscreen_time=new ArrayList<String>();
              Joining_time=new ArrayList<String>();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String allData = dataInputStream.readUTF();
                String stuName= allData.substring(allData.indexOf('*')+1,allData.indexOf(":"));
                String seatNo=allData.substring(allData.indexOf(':')+1,allData.length());
                connectClient.name =  stuName;
                connectClient.seatno= seatNo;
                Users.add(connectClient.name);
               SeatNo.add(connectClient.seatno);
//                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
//                Date currentLocalTime = cal.getTime();
//                @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:mm:ss ");
//// you can get seconds by adding  "...:ss" to it
//                date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
               Joining_time.add(getCurrentTime());





                msgLog += connectClient.name + " connected@" +
                        connectClient.socket.getInetAddress() +
                        ":" + connectClient.socket.getPort();
                deliver_lecture.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        String user="", jtime="" ,ltime="", seatNos="";
                        for(String join:Joining_time) {
                            jtime+=" "+ join;

                        }
                        for(String leave:Leaving_time) {
                            ltime+=" "+ leave;
                        }
                        for(String name:Users) {

                            user+=" "+ name;
                        }
                        for(String name:SeatNo) {

                            seatNos+=" "+ name;
                        }
//                        chatMsg.setText(msgLog +"User Added "+user +"\nJoin time: "+ Joining_time+"\nLeave time :"+ltime+"\nSeat no :"+ seatNos);
                        chatMsg.setText(msgLog +"User Added "+user);
                    }
                });

                dataOutputStream.writeUTF("Welcome " + stuName + "\n");
                dataOutputStream.flush();
                
                //broadcastMsg(allData + " join our chat.\allData");

                while (true) {
                    if (dataInputStream.available() > 0) {
                        String newMsg = dataInputStream.readUTF();


                        msgLog += stuName + " says: " + newMsg;

                        deliver_lecture.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                chatMsg.setTextColor(Color.parseColor("#0000FF")); chatMsg.setText(msgLog);
                            }
                        });

                        //broadcastMsg(allData + ": " + newMsg);
                    }

                    if(!msgToSend.equals("")){
                        dataOutputStream.writeUTF(msgToSend);
                        dataOutputStream.flush();
                        msgToSend = "";
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                userList.remove(connectClient);
                deliver_lecture.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(deliver_lecture.this,
                                connectClient.name + " removed.", Toast.LENGTH_LONG).show();

                        msgLog += "-- " + connectClient.name + " leaved\n";
                        deliver_lecture.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                chatMsg.setText(msgLog);
                            }
                        });

                        //broadcastMsg("-- " + connectClient.name + " leaved\n");
                    }
                });
            }

        }

        private void sendMsg(String msg){
            msgToSend = msg;
        }

    }

    /*private void broadcastMsg(String msg){
        for(int i=0; i<userList.size(); i++){
            userList.get(i).chatThread.sendMsg(msg);
            msgLog += "- send to " + userList.get(i).name + "\n";
        }

        deliver_lecture.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                chatMsg.setText(msgLog);
            }
        });
    }*/

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    class ChatClient {
        String name;
        Socket socket;
        String seatno;
        ConnectThread chatThread;

    }


}