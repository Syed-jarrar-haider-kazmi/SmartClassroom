package com.example.lecturefocused;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class attend_lecture extends AppCompatActivity {
    static final int SocketServerPORT = 8080;

    LinearLayout loginPanel, chatPanel;

    EditText editTextUserName, editTextAddress;
    Button buttonConnect;
    TextView chatMsg, textPort;

    EditText editTextSay;
    Button buttonSend;
    Button buttonDisconnect, openOther;
    String Seat;
    String msgLog = "";

    ChatClientThread chatClientThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_lecture);
        loginPanel = (LinearLayout)findViewById(R.id.loginpanel);
        chatPanel = (LinearLayout)findViewById(R.id.chatpanel);

        editTextUserName = (EditText) findViewById(R.id.username);
        editTextAddress = (EditText) findViewById(R.id.address);
        textPort = (TextView) findViewById(R.id.port);
        textPort.setText("port: " + SocketServerPORT);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonDisconnect = (Button) findViewById(R.id.disconnect);
        chatMsg = (TextView) findViewById(R.id.chatmsg);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonDisconnect.setOnClickListener(buttonDisconnectOnClickListener);
        openOther=(Button)findViewById(R.id.openSimilarity);

        editTextSay = (EditText)findViewById(R.id.say);
        buttonSend = (Button)findViewById(R.id.send);

        buttonSend.setOnClickListener(buttonSendOnClickListener);
        openOther.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Intent i;
                                             PackageManager manager = getPackageManager();
                                             try {
                                                 i = ((PackageManager) manager).getLaunchIntentForPackage("com.huawei.mlkit.example");
                                                 if (i == null)
                                                     throw new PackageManager.NameNotFoundException();
                                                 i.addCategory(Intent.CATEGORY_LAUNCHER);
                                                 startActivity(i);
                                             } catch (PackageManager.NameNotFoundException e) {

                                             }
                                         }


        });



    }

    View.OnClickListener buttonDisconnectOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(chatClientThread==null){
                return;
            }
            chatClientThread.disconnect();
//            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
//            Date currentLocalTime = cal.getTime();
//            @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:mm:ss ");
//// you can get seconds by adding  "...:ss" to it
//            date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
//
//
////date.format(currentLocalTime).toString()   // to get Value




        }

    };

    View.OnClickListener buttonSendOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (editTextSay.getText().toString().equals("")) {
                return;
            }

            if(chatClientThread==null){
                return;
            }

            chatClientThread.sendMsg(editTextSay.getText().toString() + "\n");
        }

    };

    View.OnClickListener buttonConnectOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String textUserName = editTextUserName.getText().toString();
            if (textUserName.equals("")) {
                Toast.makeText(attend_lecture.this, "Enter User Name",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String textAddress = editTextAddress.getText().toString();
            if (textAddress.equals("")) {
                Toast.makeText(attend_lecture.this, "Enter Address",
                        Toast.LENGTH_LONG).show();
                return;
            }

            msgLog = "";
            chatMsg.setText(msgLog);
            loginPanel.setVisibility(View.GONE);
            chatPanel.setVisibility(View.VISIBLE);
            Intent i= getIntent();
            Seat=i.getStringExtra("codes");
            Log.e("Seat No :","======"+Seat);

            chatClientThread = new ChatClientThread(
                    textUserName, textAddress, SocketServerPORT, Seat);
            chatClientThread.start();
        }

    };

    private class ChatClientThread extends Thread {

        String name;
        String dstAddress;
        int dstPort;
        String sittingSeat;

        String msgToSend = "";
        boolean goOut = false;

        ChatClientThread(String name, String address, int port, String sittingseat) {
            this.name = name;
            dstAddress = address;
            dstPort = port;
            this.sittingSeat=sittingseat;
        }

        @Override
        public void run() {
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeUTF( "*"+name +" : "+sittingSeat);
               // dataOutputStream.writeUTF( Seat);
                dataOutputStream.flush();

                while (!goOut) {
                    if (dataInputStream.available() > 0) {
                        msgLog += dataInputStream.readUTF();

                        attend_lecture.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                chatMsg.setText(msgLog);
                            }
                        });
                    }

                    if(!msgToSend.equals("")){
                        dataOutputStream.writeUTF(msgToSend);
                        dataOutputStream.flush();
                        msgToSend = "";
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                final String eString = e.toString();
                attend_lecture.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(attend_lecture.this, eString, Toast.LENGTH_LONG).show();
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
                final String eString = e.toString();
                attend_lecture.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(attend_lecture.this, eString, Toast.LENGTH_LONG).show();
                    }

                });
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
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

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                attend_lecture.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        loginPanel.setVisibility(View.VISIBLE);
                        chatPanel.setVisibility(View.GONE);
                    }

                });
            }

        }

        private void sendMsg(String msg){
            msgToSend = msg;
        }

        private void disconnect(){
            goOut = true;
        }


    }


}