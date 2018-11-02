package com.example.manoh.project515;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by manoh on 4/9/2018.
 */

public class UserRequestSentActivity extends AppCompatActivity {

    Button sent_requests,received_requests;
    String Current_User_Name1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userrequests);

        sent_requests = (Button) findViewById(R.id.RequestsSent);
        received_requests = (Button) findViewById(R.id.RequestsReceived);

        Intent intent1 = getIntent();
        Current_User_Name1 = intent1.getStringExtra("Current_User");

        // Action when sent_requests button is pressed.
        sent_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRequestSentActivity.this,UserRequestSentProcessActivity.class);
                intent.putExtra("Current_User1",Current_User_Name1);
                startActivity(intent);
            }
        });

        //Action when received_requests button is pressed
        received_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRequestSentActivity.this, UserRequestReceivedProcessActivity.class);
                intent.putExtra("Current_User2",Current_User_Name1);
                startActivity(intent);
            }
        });

    }
}
