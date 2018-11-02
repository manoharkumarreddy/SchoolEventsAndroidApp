package com.example.manoh.project515;

import java.util.*;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by manoh on 3/23/2018.
 */

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{

    //Declaring the elements that have been used in activity_admin.xml
    Button submit_event_data,set_date_event,set_time_event;
    EditText name_event,desc_event,loc_event,date_event,time_event,lat_event,long_event;
    private int mYear, mMonth, mDay, mHour, mMinute;

    //Declaring variables that are needed to store some of the values required in the program.
    Boolean CheckEditText;
    String ename_holder,edesc_holder,eloc_holder,edate_holder,etime_holder,elat_holder,elong_holder;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    String finalResult2 = "";
    HttpParse httpParse = new HttpParse();
    String HttpUrl2 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/AdminData.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Assigning id's
        name_event = (EditText) findViewById(R.id.EventName);
        desc_event = (EditText) findViewById(R.id.EventDescription);
        loc_event = (EditText) findViewById(R.id.EventLocation);
        date_event = (EditText) findViewById(R.id.EventDate) ;
        time_event = (EditText) findViewById(R.id.EventTime);
        lat_event = (EditText) findViewById(R.id.LocationLatitude);
        long_event = (EditText) findViewById(R.id.LocationLongitude);
        set_date_event = (Button) findViewById(R.id.SetDate);
        set_time_event = (Button) findViewById(R.id.SetTime);
        set_date_event.setOnClickListener(this);
        set_time_event.setOnClickListener(this);

        submit_event_data = (Button) findViewById(R.id.SubmitInfo);


       submit_event_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether edittext is empty or not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText)
                {
                    // if CheckEditText is true
                    EventDataFunction(ename_holder,edesc_holder,eloc_holder,edate_holder,etime_holder,elat_holder,elong_holder);
                }
                else
                {
                    // if CheckEditText is false
                    Toast.makeText(AdminActivity.this,"Please fill all the fields.",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    //CheckEditTextIsEmptyOrNot checks if there are any of the form fields that are not yet filled.
    public void CheckEditTextIsEmptyOrNot()
    {
        ename_holder = name_event.getText().toString();
        edesc_holder = desc_event.getText().toString();
        eloc_holder = loc_event.getText().toString();
        edate_holder = date_event.getText().toString();
        etime_holder = time_event.getText().toString();
        elat_holder = lat_event.getText().toString();
        elong_holder = long_event.getText().toString();

        if(TextUtils.isEmpty(ename_holder) || TextUtils.isEmpty(edesc_holder) || TextUtils.isEmpty(eloc_holder) || TextUtils.isEmpty(edate_holder) || TextUtils.isEmpty(etime_holder) || TextUtils.isEmpty(elat_holder) || TextUtils.isEmpty(elong_holder))
        {
            CheckEditText = false;
        }
        else
        {
            CheckEditText = true;
        }
    }

    public void EventDataFunction(final String E_name,final String E_desc,final String E_loc,final String E_date,final String E_time,final String E_lat,final String E_long)
    {
       class EventDataFunctionClass extends AsyncTask<String,Void,String>
       {
           @Override
           protected void onPreExecute()
           {
               super.onPreExecute();
               progressDialog = ProgressDialog.show(AdminActivity.this,"Data Loading",null,true,true);
           }

           @Override
           protected void onPostExecute(String httpResponseMsg)
           {
               super.onPostExecute(httpResponseMsg);
               progressDialog.dismiss();
               Intent intent = new Intent(AdminActivity.this,AdminActivity.class);
               startActivity(intent);

               Toast.makeText(AdminActivity.this,httpResponseMsg.toString(),Toast.LENGTH_LONG).show();
           }

           @Override
           protected String doInBackground(String... params)
           {
               hashMap.put("e_name",params[0]);
               hashMap.put("e_desc",params[1]);
               hashMap.put("e_loc",params[2]);
               hashMap.put("e_date",params[3]);
               hashMap.put("e_time",params[4]);
               hashMap.put("e_lat",params[5]);
               hashMap.put("e_long",params[6]);
               finalResult2 = httpParse.postRequest(hashMap,HttpUrl2);
               return finalResult2;
           }

       }
       EventDataFunctionClass eventDataFunctionClass = new EventDataFunctionClass();
       eventDataFunctionClass.execute(E_name,E_desc,E_loc,E_date,E_time,E_lat,E_long);
    }

    @Override
    public void onClick(View v)
    {
           if(v == set_date_event) {
               //Getting Date
               final Calendar c = Calendar.getInstance();
               mYear = c.get(Calendar.YEAR);
               mMonth = c.get(Calendar.MONTH);
               mDay = c.get(Calendar.DAY_OF_MONTH);

               //Launching the date picker dialog.
               DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                       date_event.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                   }
               }, mYear, mMonth, mDay);

               datePickerDialog.show();
           }
           if(v == set_time_event)
           {
               //Getting Time
               final Calendar c = Calendar.getInstance();
               mHour = c.get(Calendar.HOUR_OF_DAY);
               mMinute = c.get(Calendar.MINUTE);

               //Launching the time picker dialog
               TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       time_event.setText(hourOfDay + ":" + minute);
                   }
               },mHour, mMinute, false);
               timePickerDialog.show();
           }
    }


}
