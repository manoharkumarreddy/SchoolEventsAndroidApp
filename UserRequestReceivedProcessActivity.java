package com.example.manoh.project515;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by manoh on 4/9/2018.
 */

public class UserRequestReceivedProcessActivity extends AppCompatActivity {

    //List for storing the data
    private ArrayList<EventDataAdapter> eventDataList1 = new ArrayList<>();;
    private AdapterUserRequestReceivedProcessActivity adapter1;
    EventDataAdapter eventDataAdapter1;
    String Current_User_Name3;
    String result_value;
    TextView Accepting_Request,Rejecting_Request,Getting_Directions;
    HttpParse httpParse = new HttpParse();
   // String HttpURL6 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UsersInfoNameId.php";
    String HttpURL = "http://undcemcs02.und.edu/~manoharkumarreddy.da/EventRequestReceived.php";
    //String HttpURL1 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UserReceivedEventData.php";

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userrequestsreceived);

        //the recyclerView
        RecyclerView recyclerView;

        //getting the recyclerview from layout xml
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewRequestsReceived);

        Intent intent2 = getIntent();
        Current_User_Name3 = intent2.getStringExtra("Current_User2");
        Log.i("UserName: ", ": " +Current_User_Name3);
        /*Intent intent2 = getIntent();
        Receiver_User_Name3 = intent2.getStringExtra("Receiver_User_Name2");
        EventDataAdapter adapter2 =  new EventDataAdapter();
        if(Receiver_User_Name3 != null)
        eventDataList1.add(adapter2.setUNameReceiver(Receiver_User_Name3));*/

        adapter1 = new AdapterUserRequestReceivedProcessActivity(getApplicationContext(),eventDataList1);
        Log.i("EventdataListSize: ", ":" + eventDataList1.size());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.i("Hello", "Manohar");
        recyclerView.setAdapter(adapter1);
        Log.i("Hello", "NewYork");
        // Getting Event Name and Name of the user who sent the request to the current user.
        new GetJSONClass().execute(Current_User_Name3);
        //Log.i("Event Name", ":"+Event_Name);
        // Getting Event details by using the name we got from getJSON



    }

    //Method getJSON.
    //Parsing the JSON encode data given by the php code in server.
    // Method For Getting Event Name and Name of the user who sent the request to the current user.
        class GetJSONClass extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    URL url = new URL(HttpURL);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setReadTimeout(14000);
                    con.setConnectTimeout(14000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("u_receiver", "UTF-8") + "=" + URLEncoder.encode(Current_User_Name3, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    StringBuilder sb = new StringBuilder();
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String json;
                        while ((json = bufferedReader.readLine()) != null) {
                            sb.append(json + "\n");
                        }
                    }
                    return sb.toString().trim();
                }
                catch(Exception e)
                {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                try
                {
                    JSONArray jsonArray = new JSONArray(s);
                    Log.i("length of JSON Array: ", "mylength: " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        eventDataAdapter1 = new EventDataAdapter();
                        if (jsonObject.getString("idofreceiver") != null) {
                            String Receiver_User_Id = jsonObject.getString("idofreceiver");
                            eventDataAdapter1.setUNameReceiver1(Receiver_User_Id);
                            //eventDataList1.add(eventDataAdapter1);
                        }
                        if (jsonObject.getString("status") != null) {
                            String sending_status = jsonObject.getString("status");
                            String accept_status_info = "0";
                            String reject_status_info = "2";
                            eventDataAdapter1.setRStatus(sending_status);
                            eventDataAdapter1.setRStatus1(accept_status_info);
                            eventDataAdapter1.setRStatus2(reject_status_info);
                            //eventDataList1.add(eventDataAdapter1);
                        }
                        if (jsonObject.getString("idofuser") != null) {
                            String Sender_User_Id = jsonObject.getString("idofuser");
                            eventDataAdapter1.setUNameSender(Sender_User_Id);
                            //new ReceiverNameFunctionClass().execute(Receiver_User_Id);
                        }
                        if (jsonObject.getString("nameofuser") != null) {
                            eventDataAdapter1.setUNameSender1(jsonObject.getString("nameofuser"));
                        }
                        if (jsonObject.getString("nameofevent") != null) {
                            String Event_Name = jsonObject.getString("nameofevent");
                            eventDataAdapter1.setEName1(Event_Name);
                            //new GetJSONClass1().execute(Event_Name);
                        }
                        if (jsonObject.getString("descofevent") != null) {
                            eventDataAdapter1.setEDesc(jsonObject.getString("descofevent"));
                        }
                        if (jsonObject.getString("locofevent") != null) {
                            eventDataAdapter1.setELoc(jsonObject.getString("locofevent"));
                        }
                        if (jsonObject.getString("dateofevent") != null) {
                            eventDataAdapter1.setEDate(jsonObject.getString("dateofevent"));
                        }
                        if (jsonObject.getString("timeofevent") != null) {
                            eventDataAdapter1.setETime(jsonObject.getString("timeofevent"));
                        }
                        if(jsonObject.getString("latofevent") != null){
                            eventDataAdapter1.setELat(jsonObject.getString("latofevent"));
                        }
                        if(jsonObject.getString("longofevent") != null){
                            eventDataAdapter1.setELong(jsonObject.getString("longofevent"));
                        }
                        if(jsonObject.getString("sentuserlat") != null){
                            eventDataAdapter1.setSenderLat(jsonObject.getString("sentuserlat"));
                        }
                        if(jsonObject.getString("sentuserlong") != null){
                            eventDataAdapter1.setSenderLong(jsonObject.getString("sentuserlong"));
                        }
                        if(jsonObject.getString("receiveduserlat") != null){
                            eventDataAdapter1.setReceiverLat(jsonObject.getString("receiveduserlat"));
                        }
                        if(jsonObject.getString("receiveduserlong") != null){
                            eventDataAdapter1.setReceiverLong(jsonObject.getString("receiveduserlong"));
                        }

                        eventDataList1.add(eventDataAdapter1);
                        Log.i("event data list1 add", ":"+eventDataList1.size());
                        adapter1.notifyDataSetChanged();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
               // adapter1.notifyDataSetChanged();
            }
        }

   /* public void loadArray(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        Log.i("length of JSON Array: ", "mylength: " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            eventDataAdapter1 = new EventDataAdapter();
            if (jsonObject.getString("nameofevent") != null) {
                Saving_Event_Name = jsonObject.getString("nameofevent");
                eventDataAdapter1.setEName1(Saving_Event_Name);
            }
            if(jsonObject.getString("idofuser") != null) {
                String Receiver_User_Id = jsonObject.getString("idofuser");
                ReceiverNameFunction(Receiver_User_Id);
            }
            //getJSON1(Saving_Event_Name,HttpURL1);
            if(jsonObject.getString("status") != null)
            {
                status_value = Integer.parseInt(jsonObject.getString("status"));
                if(status_value == 1)
                {
                    eventDataAdapter1.setRStatus("Pending");
                }
                else
                {
                    eventDataAdapter1.setRStatus("Accepted");
                }
            }



            eventDataList1.add(eventDataAdapter1);
            adapter1.notifyDataSetChanged();
        }
    }}*/


        /* class ReceiverNameFunctionClass extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String answer)
            {
                super.onPostExecute(answer);
                HashMap<Integer,String> hashmapAnswer = new HashMap<>();
                String[] resulted_answer = answer.split(" ");
                for(int i = 0 ; i < resulted_answer.length; i++)
                {
                    hashmapAnswer.put(i,resulted_answer[i]);
                }
                eventDataAdapter1.setUNameReceiver1(hashmapAnswer.get(0));
                eventDataAdapter1.setUNameReceiver2(hashmapAnswer.get(1));
                eventDataList1.add(eventDataAdapter1);
                adapter1.notifyDataSetChanged();
            }

            @Override
            protected String doInBackground(String... params)
            {
                HashMap<String,String> hashMap1 = new HashMap<>();
                hashMap1.put("u_id1",params[0]);
                result_value = httpParse.postRequest(hashMap1,HttpURL6);
                return result_value;
            }
        }

    // Method for Getting Event details by using the name we got from getJSON
        class GetJSONClass1 extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    URL url = new URL(HttpURL1);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setReadTimeout(14000);
                    con.setConnectTimeout(14000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("event_name", "UTF-8") + "=" + URLEncoder.encode(Event_Name, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine()) != null)
                    {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                }
                catch(Exception e)
                {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                try
                {
                    JSONArray jsonArray1 = new JSONArray(s);
                    Log.i("length of JSON Array: ", "mylength: " + jsonArray1.length());
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        eventDataAdapter1 = new EventDataAdapter();
                        if(jsonObject1.getString("nameofevent") != null)
                        {
                            eventDataAdapter1.setEName1(jsonObject1.getString("nameofevent"));
                        }
                        if (jsonObject1.getString("descofevent") != null) {
                            eventDataAdapter1.setEDesc(jsonObject1.getString("descofevent"));
                        }
                        if (jsonObject1.getString("locofevent") != null) {
                            eventDataAdapter1.setELoc(jsonObject1.getString("locofevent"));
                        }
                        if (jsonObject1.getString("dateofevent") != null) {
                            eventDataAdapter1.setEDate(jsonObject1.getString("dateofevent"));
                        }
                        if (jsonObject1.getString("timeofevent") != null) {
                            eventDataAdapter1.setETime(jsonObject1.getString("timeofevent"));
                        }
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }*/


   /* public void loadArray1(String json) throws JSONException {
        JSONArray jsonArray1 = new JSONArray(json);
        Log.i("length of JSON Array: ", "mylength: " + jsonArray1.length());
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
            eventDataAdapter1 = new EventDataAdapter();
            if(jsonObject1.getString("descofevent") != null)
            {
                eventDataAdapter1.setEDesc(jsonObject1.getString("descofevent"));
            }
            if(jsonObject1.getString("locofevent") != null)
            {
                eventDataAdapter1.setELoc(jsonObject1.getString("locofevent"));
            }
            if(jsonObject1.getString("dateofevent") != null)
            {
                eventDataAdapter1.setEDate(jsonObject1.getString("dateofevent"));
            }
            if(jsonObject1.getString("timeofevent") != null)
            {
                eventDataAdapter1.setETime(jsonObject1.getString("timeofevent"));
            }

            eventDataList1.add(eventDataAdapter1);
            //adapter1.notifyDataSetChanged();
        }
    }*/



}
