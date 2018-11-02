package com.example.manoh.project515;

/**
 * Created by manoh on 3/28/2018.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.cardemulation.HostApduService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UserActivity extends FragmentActivity implements LocationListener,GoogleMap.OnMarkerClickListener{
    GoogleMap googleMap;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> arrayList1 = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();
    String eventName = "",eventDesc = "",eventLoc = "",eventDate = "",eventTime = "",userloclat="",userloclong="",eventName1="";
    Marker marker1,marker2;
    String HttpURL3 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UserData.php";
    String HttpURL4 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UserLatLongInsert.php";
    String HttpURL5 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UserLatLongSelect.php";
    String HttpURL6 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UsersInfoNameId.php";
    String HttpURL7 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/InvitationInserted.php";
    String HttpURL8 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UserEventSelect.php";
    String result1 = "",result2 = "",result3 = "";
    HttpParse httpParse = new HttpParse();
    String olderLat="",olderLong="",userid="",User_Id_Holder, Invite_Event_Holder,Receiver_Id_Holder="",Receiver_Name_Holder="";
    String invitation_status = "1";
    TextView selectedEvent, requests_hangout_sent;
    Dialog dialog;
    private Handler handler;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        // Show error dialog if GoolglePlayServices not available.
        if ( !isGooglePlayServicesAvailable( ) ) {
            finish( );
        }
        setContentView( R.layout.activity_user );

        //Sending the URL to parsing method.
        getJSON(HttpURL3);

        //Getting the user location.
        try {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment supportMapFragment =
                    (SupportMapFragment) getSupportFragmentManager().
                            findFragmentById(R.id.map);
            googleMap = supportMapFragment.getMap();
            googleMap.setMyLocationEnabled(true);
            // Use the LocationManager class to obtain GPS locations.
            LocationManager locationManager =
                    (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            //getJSON(HttpURL3);
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
            handler = new Handler();

        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }

       // Other_User_Holder = gotUserName.getText().toString();
        requests_hangout_sent = (TextView) findViewById(R.id.RequestsData);
        requests_hangout_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,UserRequestSentActivity.class);
                intent.putExtra("Current_User",User_Id_Holder);
                startActivity(intent);
            }
        });

        /*Intent intent = getIntent();
        double receiverlat = Double.parseDouble(intent.getStringExtra("reclat"));
        Log.i("latitude receiver: ", Double.toString(receiverlat));
        double receiverlong = Double.parseDouble(intent.getStringExtra("reclong"));
        double eventlat = Double.parseDouble(intent.getStringExtra("evelat"));
        double eventlong = Double.parseDouble(intent.getStringExtra("evelong"));
        LatLng origin = new LatLng(receiverlat, receiverlong);
        LatLng destination = new LatLng(eventlat, eventlong);
        //Building the URL including Google Firection API
        String url = getDirectionsUrl(origin, destination);
        new DownloadTask().execute(url);*/
    }

    /*private String getDirectionsUrl( LatLng got_origin, LatLng got_destination)
    {
        // Origin of route
        String str_origin = "origin=" + got_origin.latitude + "," + got_origin.longitude;
        // Destination of route
        String str_dest  = "destination=" + got_destination.latitude + "," + got_destination.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the URL for the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.i("URL of getDirections: ", url);
        return url;
    }

    // A class to download data from Google Directions URL
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground( String... url ) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl( url[0] );
            }
            catch( Exception e ) {
                Log.d( "Background Task", e.toString( ) );
            }
            return data;
        }  // End of doInBackground


        // Executes in UI thread, after the execution of doInBackground( )
        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            Log.i("Result: ", result);
            ParserTask parserTask = new ParserTask( );
            // Invokes the thread for parsing the JSON data.
            parserTask.execute( result );
        }  // End of onPostExecute

    }  // End of DownloadTask

    // A method to download JSON data from URL
    private String downloadUrl( String strUrl ) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL( strUrl );
            // Creating an HTTP connection to communicate with URL
            urlConnection = (HttpURLConnection) url.openConnection( );
            // Connecting to URL
            urlConnection.connect( );
            // Reading data from URL
            iStream = urlConnection.getInputStream( );
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( iStream ) );
            StringBuffer sb  = new StringBuffer( );
            String line = "";
            while( ( line = br.readLine( ) ) != null ) {
                sb.append( line );
            }
            data = sb.toString( );
            Log.i("Data of downLoad URL", data);
            br.close( );
        }  // End of try
        catch( Exception e ) {
            Log.d( "Exception URL Download", e.toString( ) );
        }
        finally {
            iStream.close( );
            urlConnection.disconnect( );
        }
        return data;
    }  // End of downloadUrl

    // A class to parse the Google Directions in JSON format
    private class ParserTask extends AsyncTask<String, Integer,
            List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>>
        doInBackground( String... jsonData ) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject( jsonData[0] );
                DirectionJSONParser parser = new DirectionJSONParser( );
                // Starts parsing data.
                routes = parser.parse( jObject );
            }
            catch( Exception e ) {
                e.printStackTrace( );
            }
            //Log.i("Routes: ", routes);
            return routes;
        }  // End of doInBackground


        // Executes in UI thread, after the parsing process.
        @Override
        protected void onPostExecute(
                List<List<HashMap<String, String>>> result ) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for ( int i=0; i<result.size( ); i++ ) {
                points = new ArrayList<LatLng>( );
                lineOptions = new PolylineOptions( );
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get( i );

                // Fetching all the points in i-th route
                for ( int j=0; j<path.size( ); j++ ) {
                    HashMap<String,String> point = path.get( j );
                    double lat = Double.parseDouble( point.get( "lat" ) );
                    Log.i("lati: ", Double.toString(lat));
                    double lng = Double.parseDouble( point.get( "lng" ) );
                    Log.i("long: ", Double.toString(lng));
                    LatLng position = new LatLng( lat, lng );
                    points.add( position );
                }  // End of inner for

                // Adding all the points in the route to LineOptions
                lineOptions.addAll( points );
                lineOptions.width( 2 );
                lineOptions.color( Color.RED );
            }  // End of outer for

            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline( lineOptions );
        }  // End of onPostExecute

    }  // End of ParserTask*/

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            googleMap.clear();
            getJSON(HttpURL3);
            getJSON(HttpURL5);
            /*if((new_send_lat == sendLat) && (new_send_long == sendLong))
            {
                //do Nothing
            }
            else
            {
                sendLat = new_send_lat;
                sendLong = new_send_long;
            }
            drawMarker(receiverLat,receiverLong,sendLat,sendLong,eveLat,eveLong);*/
            handler.postDelayed(updateTimerThread,4000);
        }
    };

    //Parsing the JSON encode data given by the php code in server.
    public void getJSON(final String HttpURL)
    {
        class GetJSONClass extends AsyncTask<Void,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids)
            {
                try
                {
                    URL url = new URL(HttpURL);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
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
                   if(HttpURL.equalsIgnoreCase(HttpURL3)) {
                       loadIntoArray(s);
                   }
                   else {
                       loadIntoArray1(s);
                   }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        GetJSONClass getJSONClass = new GetJSONClass();
        getJSONClass.execute();
    }

    //public void getJSON1(final String U_sender,final String U_receiver,final String HttpURL)
    //{
        class GetJSONClass1 extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                //Log.i("data of sender receiver", "getJSON1: " + U_sender + ":" + U_receiver);
                Log.i("entering doInBackground", "Entered");
                try {
                    URL url = new URL(HttpURL8);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(14000);
                    con.setConnectTimeout(14000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("u_sender", "UTF-8") + "=" + URLEncoder.encode(User_Id_Holder, "UTF-8");
                    data += "&" + URLEncoder.encode("u_receiver", "UTF-8") + "=" + URLEncoder.encode(Receiver_Id_Holder, "UTF-8");
                    Log.i("data going:", data);
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    StringBuilder sb = new StringBuilder();
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String json = null;
                        while ((json = bufferedReader.readLine()) != null) {
                            sb.append(json + "\n");
                        }
                    } else {
                        Log.i("wrong", "Something went wrong");
                    }
                    Log.i("output json", "doInBackground: " + sb.toString().trim());
                    return sb.toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    if (s != null) {
                        Log.i("eventstring", "onPostExecute: " + s);
                        JSONArray jsonArray2 = new JSONArray(s);
                        Log.i("size :", "loadIntoArray2: " + jsonArray2.length());
                        arrayList2.clear();
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                            Log.i("Entering JSON2", "the values are entered.");
                            if (jsonObject2.getString("nameofevent") != null) {
                                eventName1 = jsonObject2.getString("nameofevent");
                                Log.i("event name", "Name of Event :" + eventName1);
                                arrayList2.add(eventName1);
                            }
                        }
                    } else {
                        Log.i("string status", ":empty");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                generateDialog(arrayList2);
            }
        }
    //        GetJSONClass1 getJSONClass1 = new GetJSONClass1();
     //       getJSONClass1.execute();
    //}

    public void generateDialog(ArrayList<String> getSavingData){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_userdialog);
        dialog.setTitle("Wanna Hangout???");

        TextView gotUserName = (TextView) dialog.findViewById(R.id.FriendNameValue);
        gotUserName.setText(Receiver_Name_Holder);

        Spinner gotEventNames = (Spinner) dialog.findViewById(R.id.EventSelectionOptions);
        ArrayList<String> variousEventNames = new ArrayList<String>();
        Log.i("size::", "arraylist2: "+getSavingData.size());
        for (int n=0; n<getSavingData.size(); n++)
        {
            variousEventNames.add(getSavingData.get(n));
        }

        Log.i("Hello", "Mohan");
        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variousEventNames);
        //Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Attaching data adapter to spinner
        gotEventNames.setAdapter(dataAdapter);

        gotEventNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedEvent = (TextView) dialog.findViewById(R.id.EventSelectionValue);
                selectedEvent.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Implementing the CloseDialog button
        Button closingDialog = (Button) dialog.findViewById(R.id.DialogClose);
        closingDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        //Implementing the Invitation Button
        Button hangoutInvite = (Button) dialog.findViewById(R.id.SendingInvitation);
        hangoutInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Invite_Event_Holder = selectedEvent.getText().toString();
                InvitationFunction(User_Id_Holder,Invite_Event_Holder,Receiver_Id_Holder,invitation_status);
                dialog.dismiss();
            }
        });

        //Showing the dialog.
        dialog.show();

    }

    //Storing the data in array of hashmap and drawing the markers based on data.
    public void loadIntoArray(String json1) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(json1);
        arrayList.clear();
        Log.i("length of JSON Array: ", "mylength: "+jsonArray.length());
        for(int i=0; i<jsonArray.length();i++)
        {
            JSONObject object = jsonArray.getJSONObject(i);
            HashMap<String,String> hashMap = new HashMap<>();
            if(object.getString("nameofevent") != null) {
                eventName = object.getString("nameofevent");
                hashMap.put("nameEvent",eventName);
            }
            if(object.getString("descofevent") != null) {
                eventDesc = object.getString("descofevent");
                hashMap.put("descEvent",eventDesc);
            }
            if(object.getString("locofevent") != null) {
                eventLoc = object.getString("locofevent");
                hashMap.put("locEvent",eventLoc);
            }
            if(object.getString("dateofevent") != null)
            {
                eventDate = object.getString("dateofevent");
                hashMap.put("dateEvent",eventDate);
            }
            if(object.getString("timeofevent") != null)
            {
                eventTime = object.getString("timeofevent");
                hashMap.put("timeEvent",eventTime);
            }
           /* if(object.getString("latofevent") != null)
            {
                eventLat = object.getString("latofevent");
                hashMap.put("latEvent",eventTime);
            }
            if(object.getString("longofevent") != null)
            {
                eventLong = object.getString("longofevent");
                hashMap.put("longEvent",eventTime);
            }*/

            //Getting the current date.
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String curr_date = sdf.format(c.getTime());

            if(eventDate.compareToIgnoreCase(curr_date) == 0) {
                marker1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(object.getString("latofevent")), Double.parseDouble(object.getString("longofevent")))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                marker1.setTitle("Event"+eventName);
            }
            else if(eventDate.compareToIgnoreCase(curr_date) < 0)
            {
                //Do Nothing which means no marker will be drawn.
            }
            else {
                marker1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(object.getString("latofevent")), Double.parseDouble(object.getString("longofevent")))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                marker1.setTitle("Event"+eventName);
            }

            //marker1.setSnippet("Name: \n");  //eventName+"\nDescription: \n"+eventDesc+"\nLocation: \n"+eventLoc+"\nDate: \n"+eventDate+"\nTime: \n"+eventTime);
            //marker1.setSnippet(eventName);

            arrayList.add(hashMap);
            //googleMap.setOnMarkerClickListener(this);
        }
        Log.i("eventarraylistsize", "myeventsize: "+arrayList.size());
       googleMap.setOnMarkerClickListener(this);

    }

    //Loading the events data on which user can send request into an arrayList.
   /* public ArrayList<String> loadIntoArray2(String json3) throws JSONException
    {
        JSONArray jsonArray2 = new JSONArray(json3);
        Log.i("size :", "loadIntoArray2: "+jsonArray2.length());
        arrayList2.clear();
        for(int i=0; i<jsonArray2.length(); i++)
        {
            JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
            Log.i("Entering JSON2", "the values are entered.");
            if(jsonObject2.getString("nameofevent") != null)
            {
                eventName1 = jsonObject2.getString("nameofevent");
                Log.i("event name", "Name of Event :"+eventName1);
                arrayList2.add(eventName1);
            }
        }
        return arrayList2;
    }*/

    //Method for onMarkerClick.
    public boolean onMarkerClick(Marker marker)
    {
        //Log.i("hello","onMarker: Clicked");
        //Creating a dialog that contains event information
        //final Dialog dialog = new Dialog(this);
        //dialog.setContentView(R.layout.activity_dialog);
       // Log.i("arrayList", "Size:" + arrayList.size());

        //For loop for event markers.
        if(marker.getTitle().substring(0,5).equalsIgnoreCase("Event")) {
            String eventTitle = marker.getTitle().substring(5);
            for (int i = 0; i < arrayList.size(); i++) {
                HashMap<String, String> hashMap = arrayList.get(i);
                //Log.i("market_details",marker.getTitle()+ ":" +hashMap.get("nameEvent"));
                if (eventTitle.equalsIgnoreCase(hashMap.get("nameEvent"))) {

                    //Log.i("hey","entered: if");
                    final String nameField = hashMap.get("nameEvent");
                    final String descField = hashMap.get("descEvent");
                    final String locField = hashMap.get("locEvent");
                    final String dateField = hashMap.get("dateEvent");
                    final String timeField = hashMap.get("timeEvent");

                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            View v = getLayoutInflater().inflate(R.layout.activity_dialog, null);
                            TextView NameTextView = (TextView) v.findViewById(R.id.EventNameValue);
                            TextView DescTextView = (TextView) v.findViewById(R.id.EventDescValue);
                            TextView LocTextView = (TextView) v.findViewById(R.id.EventLocValue);
                            TextView DateTextView = (TextView) v.findViewById(R.id.EventDateValue);
                            TextView TimeTextView = (TextView) v.findViewById(R.id.EventTimeValue);
                            NameTextView.setText(nameField);
                            DescTextView.setText(descField);
                            LocTextView.setText(locField);
                            DateTextView.setText(dateField);
                            TimeTextView.setText(timeField);
                            return v;
                        }
                    });
                    marker.showInfoWindow();
                }
            }
        }
        else if(marker.getTitle().substring(0,4).equalsIgnoreCase("User")) { //User Markers
            final String idField = marker.getTitle().substring(4);
            //Setting the custom dialog components
            Log.i("receiver user id", ": "+idField);
            new UserNameSelectFunctionClass().execute(idField);
            //getJSON1(User_Id_Holder,idField,HttpURL8);
            /*dialog = new Dialog(this);
            dialog.setContentView(R.layout.activity_userdialog);
            dialog.setTitle("Wanna Hangout???");

            Spinner gotEventNames = (Spinner) dialog.findViewById(R.id.EventSelectionOptions);
            ArrayList<String> variousEventNames = new ArrayList<String>();
            //Log.i("size of arrayList", "mysize: "+arrayList.size());
            for (int k = 0; k < arrayList.size(); k++) {
                HashMap<String, String> hashMap3 = arrayList.get(k);
                Log.i("Hello", "arrayList");
                //Getting the current date.
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String curr_date = sdf.format(c.getTime());
                if ((hashMap3.get("dateEvent").compareToIgnoreCase(curr_date)) >= 0) {
                    Log.i("Hello", "Manohar");
                    variousEventNames.add(hashMap3.get("nameEvent"));
                } else {
                    //Nothing should be performed.
                }
            }

            getJSON1(User_Id_Holder,idField,HttpURL8);
            Log.i("size::", "arraylist2: "+arrayList2.size());
           for (int n=0; n<arrayList2.size(); n++)
           {
               variousEventNames.add(arrayList2.get(n));
           }

            Log.i("Hello", "Mohan");
            //Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variousEventNames);
            //Drop down layout style
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Attaching data adapter to spinner
            gotEventNames.setAdapter(dataAdapter);

            gotEventNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedEvent = (TextView) dialog.findViewById(R.id.EventSelectionValue);
                    selectedEvent.setText(adapterView.getItemAtPosition(i).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            //Implementing the CloseDialog button
            Button closingDialog = (Button) dialog.findViewById(R.id.DialogClose);
            closingDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });

            //Implementing the Invitation Button
            Button hangoutInvite = (Button) dialog.findViewById(R.id.SendingInvitation);
            hangoutInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Invite_Event_Holder = selectedEvent.getText().toString();
                    InvitationFunction(User_Id_Holder,Invite_Event_Holder,idField,Integer.toString(invitation_status));
                    dialog.dismiss();
                }
            });

            //Showing the dialog.
            dialog.show();*/

        }



        return true;
    }

    //Method to insert data into invitation data table.
    public void InvitationFunction(final String User_sender,final String Event_id1,final String User_receiver,final String invite_status)
    {
        class InvitationFunctionClass extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String invitation_answer)
            {
                super.onPostExecute(invitation_answer);
                Toast.makeText(UserActivity.this,invitation_answer,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params)
            {
                HashMap<String,String> hashMap1 = new HashMap<String, String>();
                hashMap1.put("user_sender",params[0]);
                hashMap1.put("event_id1",params[1]);
                hashMap1.put("user_receiver",params[2]);
                hashMap1.put("invite_status",params[3]);
                result3 = httpParse.postRequest(hashMap1,HttpURL7);
                return result3;
            }


        }
        InvitationFunctionClass invitationFunctionClass = new InvitationFunctionClass();
        invitationFunctionClass.execute(User_sender,Event_id1,User_receiver,invite_status);
    }


    //Method onLocationChanged is called when the location is updated.
    @Override
    public void onLocationChanged( Location location ) {
        googleMap.clear();
        getJSON(HttpURL3);
         // marker.remove();
        //TextView locationTv = (TextView) findViewById( R.id.latlongLocation );
        String Latitude_Holder     = Double.toString(location.getLatitude( ));
        String Longitude_Holder    = Double.toString(location.getLongitude( ));
        Log.i("hello1", "onLocationChanged: "+Latitude_Holder);
        //getting the data passed through intent.
        Intent intent = getIntent();
        User_Id_Holder = intent.getStringExtra("IdOfUser");

        if((Latitude_Holder.equalsIgnoreCase(olderLat)) || (Longitude_Holder.equalsIgnoreCase(olderLong)))
        {
            //Do Nothing.
        }
        else {
            olderLat = Latitude_Holder;
            olderLong = Longitude_Holder;
            //Function that sends data to php code on server for inserting latitude and longitude of location of user.
            User_Latlong_Insert_Function(User_Id_Holder, Latitude_Holder, Longitude_Holder);
            Log.i("hello2", "onLocationChanged: got latitude");
        }

        //Function that read data from php code on server for marking the locations of users.
        getJSON(HttpURL5);
         Log.i("hello3","got location");
        //LatLng latLng       = new LatLng( latitude, longitude );
        // Mark locations.

        //googleMap.addMarker(new MarkerOptions()
         //           .position(latLng)
           //         .title("Current User"));

            // Move the camera instantly to the location with a zoom of 14.
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

            // Zoom in and animate the camera.
           // googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

        //getJSON(HttpURL3);
       // locationTv.setText( "Latitude: " + latitude + "\nLongitude: " + longitude );
    }

    //Loading the data into an array of hashmaps.
    public void loadIntoArray1(String json2) throws JSONException
    {
        JSONArray jsonArray1 = new JSONArray(json2);
        Log.i("hello4", "loadIntoArray1: "+jsonArray1.length());
        for (int i=0; i<jsonArray1.length();i++)
        {
            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
            HashMap<String,String> hashMap2 = new HashMap<>();
            if(jsonObject1.getString("latofuser") != null) {
                userloclat = jsonObject1.getString("latofuser");
                hashMap2.put("UserLocLat",userloclat);
            }
            if(jsonObject1.getString("longofuser") != null) {
                userloclong = jsonObject1.getString("longofuser");
                hashMap2.put("UserLocLong",userloclong);
            }
            if(jsonObject1.getString("idofuser") != null) {
                userid = jsonObject1.getString("idofuser");
                hashMap2.put("UsersId",userid);
            }

            Log.i("hello5", "loadIntoArray1: marker start");
            if(userid.equalsIgnoreCase(User_Id_Holder)) {
                marker2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(userloclat), Double.parseDouble(userloclong))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                marker2.setTitle("SelfUser"+userid);
            }
            else {
                marker2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(userloclat), Double.parseDouble(userloclong))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                marker2.setTitle("User"+userid);
            }
            Log.i("hello6", "loadIntoArray1: marker end");
            arrayList1.add(hashMap2);
        }
        googleMap.setOnMarkerClickListener(this);
    }

    //Drawing the markers of events by taking their latitudes and longitudes from eventdatatable.
    /*public void onMapReady(GoogleMap map)
    {
        Log.i("CheckOnMapReady", "onMapReady: Called");
       googleMap = map;
       for(int i=0; i<arrayList.size();i++)
       {
           hashMap = arrayList.get(i);
           MarkerOptions markerOptions = new MarkerOptions();
           markerOptions.position(new LatLng(Double.parseDouble(hashMap.get("latEvent")),Double.parseDouble(hashMap.get("longEvent"))));
           markerOptions.title("Event Information");

           //CustomInfoWindowGoogleMap customInfoWindowGoogleMap = new CustomInfoWindowGoogleMap(this);
           //GoogleMap.setInfoWindowAdapter(customInfoWindowGoogleMap)

       }
    }*/



    //User_Latlong_Function sending parameters to HttpParse.java
    public void User_Latlong_Insert_Function(final String User_id,final String User_lat,final String User_long)
    {
        class User_LatLong_Insert_FunctionClass extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String httpresponseMsg)
            {
                super.onPostExecute(httpresponseMsg);
            }

            @Override
            protected String doInBackground(String... params)
            {
                HashMap<String,String> hashMap1 = new HashMap<>();
                hashMap1.put("user_id",params[0]);
                hashMap1.put("user_lat",params[1]);
                hashMap1.put("user_long",params[2]);
                result1 = httpParse.postRequest(hashMap1,HttpURL4);
                return result1;
            }
        }

        User_LatLong_Insert_FunctionClass user_latLong_insert_functionClass = new User_LatLong_Insert_FunctionClass();
        user_latLong_insert_functionClass.execute(User_id,User_lat,User_long);
    }


        class UserNameSelectFunctionClass extends AsyncTask<String,Void,String>
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
               // TextView gotUserName = (TextView) dialog.findViewById(R.id.FriendNameValue);

                Receiver_Name_Holder = hashmapAnswer.get(0);
                Receiver_Id_Holder = hashmapAnswer.get(1);
                new GetJSONClass1().execute(User_Id_Holder,Receiver_Id_Holder,HttpURL8);

            }

            @Override
            protected String doInBackground(String... params)
            {
                HashMap<String,String> hashMap1 = new HashMap<>();
                hashMap1.put("u_id1",params[0]);
                result2 = httpParse.postRequest(hashMap1,HttpURL6);
                return result2;
            }
        }



    @Override
    public void onProviderDisabled( String provider ) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled( String provider ) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged( String provider, int status, Bundle extras ) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable( ) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable( this );
        if ( ConnectionResult.SUCCESS == status ) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog( status, this, 0 ).show( );
            return false;
        }
    }
} // End of UserActivity