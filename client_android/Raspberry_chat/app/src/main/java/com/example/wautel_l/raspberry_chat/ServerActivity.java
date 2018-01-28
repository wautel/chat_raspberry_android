package com.example.wautel_l.raspberry_chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ServerActivity extends AppCompatActivity {

    private EditText ipet;
    private EditText portet;
    private Button gobut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        ipet = (EditText) findViewById(R.id.ip);
        portet = (EditText) findViewById(R.id.port);
        gobut = (Button) findViewById(R.id.go);

        gobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(ServerActivity.this, ChatActivity.class);
                start.putExtra("ip", ipet.getText().toString());
                start.putExtra("port", portet.getText().toString());
                startActivity(start);
            }
        });

    }
}
