package com.example.manoh.project515;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

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
import java.util.List;

/**
 * Created by manoh on 4/9/2018.
 */

public class UserRequestSentProcessActivity extends AppCompatActivity {

    //List for storing the data
    private ArrayList<EventDataAdapter> eventDataList1 = new ArrayList<>();;
    private AdapterUserRequestSentProcessActivity adapter1;
    EventDataAdapter eventDataAdapter1;
    String Current_User_Name2;
    String result_value;
    String status_value;
    HttpParse httpParse = new HttpParse();
    String HttpURL6 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UsersInfo.php";
    String HttpURL = "http://undcemcs02.und.edu/~manoharkumarreddy.da/EventRequestSent.php";

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userrequestssent);

        //the recyclerView
        RecyclerView recyclerView;

        //getting the recyclerview from layout xml
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewRequestsSent);

        Intent intent2 = getIntent();
        Current_User_Name2 = intent2.getStringExtra("Current_User1");
        /*EventDataAdapter adapter2 =  new EventDataAdapter();
        if(Receiver_User_Name3 != null)
        eventDataList1.add(adapter2.setUNameReceiver(Receiver_User_Name3));*/

        adapter1 = new AdapterUserRequestSentProcessActivity(getApplicationContext(),eventDataList1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter1);

        getJSON(Current_User_Name2,HttpURL);

        //adapter1 = new AdapterUserRequestSentProcessActivity(eventDataList1);

       // adapter1.notifyDataSetChanged();
    }

    //Method getJSON.
    //Parsing the JSON encode data given by the php code in server.
    public void getJSON(final String U_sender,final String HttpURL)
    {
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
                    String data = URLEncoder.encode("u_sender", "UTF-8") + "=" + URLEncoder.encode(U_sender, "UTF-8");
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
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    Log.i("length of JSON Array: ", "mylength: " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        eventDataAdapter1 = new EventDataAdapter();
                        if (jsonObject.getString("nameofevent") != null) {
                            eventDataAdapter1.setEName(jsonObject.getString("nameofevent"));
                        }
                        if (jsonObject.getString("status") != null) {
                            status_value = jsonObject.getString("status");
                            if (status_value.equalsIgnoreCase("1")) {
                                eventDataAdapter1.setRStatus("Pending");
                            } else if(status_value.equalsIgnoreCase("0")){
                                eventDataAdapter1.setRStatus("Accepted");
                            } else {
                                eventDataAdapter1.setRStatus("Rejected");
                            }
                        }

                        if(jsonObject.getString("locofevent") != null){
                            eventDataAdapter1.setELoc1(jsonObject.getString("locofevent"));
                        }

                        if(jsonObject.getString("latofevent") != null){
                            eventDataAdapter1.setELat1(jsonObject.getString("latofevent"));
                        }
                        if(jsonObject.getString("longofevent") != null){
                            eventDataAdapter1.setELong1(jsonObject.getString("longofevent"));
                        }
                        if(jsonObject.getString("sentuserlat") != null){
                            eventDataAdapter1.setSenderLat1(jsonObject.getString("sentuserlat"));
                        }
                        if(jsonObject.getString("sentuserlong") != null){
                            eventDataAdapter1.setSenderLong1(jsonObject.getString("sentuserlong"));
                        }
                        if(jsonObject.getString("receiveduserlat") != null){
                            eventDataAdapter1.setReceiverLat1(jsonObject.getString("receiveduserlat"));
                        }
                        if(jsonObject.getString("receiveduserlong") != null){
                            eventDataAdapter1.setReceiverLong1(jsonObject.getString("receiveduserlong"));
                        }


                        if (jsonObject.getString("idofreceiver") != null) {
                            String Receiver_User_Id = jsonObject.getString("idofreceiver");
                            new ReceiverNameFunctionClass().execute(Receiver_User_Id);
                        }
                    }
                    //eventDataList1.add(eventDataAdapter1);
                    //adapter1 = new AdapterUserRequestSentProcessActivity(eventDataList1);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                adapter1.notifyDataSetChanged();

            }
        }

        GetJSONClass getJSONClass = new GetJSONClass();
        getJSONClass.execute();
        //adapter1 = new AdapterUserRequestSentProcessActivity(eventDataList1);
        //adapter1.notifyDataSetChanged();
    }

    /*public void loadArray(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        Log.i("length of JSON Array: ", "mylength: " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            eventDataAdapter1 = new EventDataAdapter();
            if (jsonObject.getString("nameofevent") != null) {
                eventDataAdapter1.setEName(jsonObject.getString("nameofevent"));
            }
            if(jsonObject.getString("idofreceiver") != null) {
                String Receiver_User_Id = jsonObject.getString("idofreceiver");
                ReceiverNameFunction(Receiver_User_Id);
            }
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
        //adapter1.notifyDataSetChanged();
    }*/

    class ReceiverNameFunctionClass extends AsyncTask<String,Void,String>
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
                eventDataAdapter1.setUNameReceiver(answer);
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

       // ReceiverNameFunctionClass receiverNameFunctionClass = new ReceiverNameFunctionClass();
        //receiverNameFunctionClass.execute(U_id1);




    /*public void getNameReceiver(String receiver_answer)
    {
       eventDataAdapter1.setUNameReceiver(receiver_answer);
       eventDataList1.add(eventDataAdapter1);
       adapter1.notifyDataSetChanged();
    }*/



}
