package com.example.wautel_l.raspberry_chat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.EmptyStackException;


/**
 * Created by wautel_l on 24/01/2018.
 */

public class ChatActivity extends Activity {

    private ListView myList;
    private ArrayList<String> arrayList;
    private MyAdapter mAdapter;
    private LocalService myClient = null;
    private connectTask myTask = null;
    private Boolean mBound;
    private String port;
    private String ip;
    private EditText myedit;
    private Button envoyer;
    private String message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatlayout);

        arrayList = new ArrayList<String>();

        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        port = intent.getStringExtra("port");
        myedit = (EditText) findViewById(R.id.myedit);
        envoyer = (Button) findViewById(R.id.sendbut);

        myList = (ListView) findViewById(R.id.myList);
        mAdapter = new MyAdapter(this, arrayList);
        myList.setAdapter(mAdapter);

        myClient = null;
        myTask = new connectTask();
        Log.e("ip", ip);
        Log.e("port", port);
        myTask.setinfos(ip, Integer.parseInt(port));
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = myedit.getText().toString();
                arrayList.add("Android : " + message);
                if (myClient != null) {
                    myClient.sendMessage("Android :" + message);
                }
                mAdapter.notifyDataSetChanged();
                myedit.setText("");
            }

        });
    }

    public class connectTask extends AsyncTask<String, String, LocalService> {

        String  ip;
        int port;

        @Override
        protected LocalService doInBackground(String... message) {
            myClient = new LocalService(new LocalService.OnMessageReceived() {
                @Override
                public void messageReceived(String message) {
                    try {
                        publishProgress(message);
                        if (message != null) {
                            System.out.println("Return message from socket");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            myClient.setinfos(ip, port);
            myClient.run();
            if (myClient != null) {
                myClient.sendMessage("Initial Message");
            }
            return null;
        }

        protected void setinfos(String ip, int port)
        {
            this.ip = ip;
            this.port = port;
        }

        @Override
        protected void onProgressUpdate(String... datas)
        {
            super.onProgressUpdate(datas);

            arrayList.add(datas[0]);

            mAdapter.notifyDataSetChanged();
        }


        protected void onDestroy()
        {
            try{
                myClient.stopClient();
                myTask.cancel(true);
                myTask = null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
