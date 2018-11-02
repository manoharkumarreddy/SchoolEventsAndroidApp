package com.example.manoh.project515;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class UserDirectionActivity extends FragmentActivity  implements LocationListener{
    GoogleMap mGoogleMap;
    //ArrayList<Double> users_lat_long = new ArrayList<>();
    LatLng user_origin,destination,sender_origin;
    String eveName,sendName,eveLoc,url,url1;
    double eveLat,eveLong,sendLat,sendLong,receiverLat,receiverLong,new_send_lat,new_send_long;
    Marker marker1,marker2,marker3;
    String HttpURL = "http://undcemcs02.und.edu/~manoharkumarreddy.da/GettingUserLocationUpdates.php";
    private Handler handler;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_userseventdirections );

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(
                getBaseContext( ) );
        if ( status != ConnectionResult.SUCCESS ) {
            // Google Play Services are not available.
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog (
                    status, this, requestCode );
            dialog.show( );
        }
        else {
            // Google Play Services are available.
            //gettiing the data from intent
            Intent intent = getIntent();
            eveName = intent.getStringExtra("eventname");
            sendName = intent.getStringExtra("sendername");
            eveLoc = intent.getStringExtra("eventlocation");
            receiverLat= Double.parseDouble(intent.getStringExtra("reclat"));
            receiverLong = Double.parseDouble(intent.getStringExtra("reclong"));
            sendLat = Double.parseDouble(intent.getStringExtra("senderlat"));
            sendLong = Double.parseDouble(intent.getStringExtra("senderlong"));
            eveLat = Double.parseDouble(intent.getStringExtra("evelat"));
            eveLong = Double.parseDouble(intent.getStringExtra("evelong"));
            //marker1 = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(sendLat,sendLong)).title(sendName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            // marker2 = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(receiverLat,receiverLong)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            //marker3 = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(eveLat,eveLong)).title(eveName).snippet("Event Location: "+eveLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

            try {

                // Getting reference to SupportMapFragment of the activity_userseventdirections
                SupportMapFragment fm = (SupportMapFragment)
                        getSupportFragmentManager().findFragmentById(R.id.map);
                // Getting Map for the SupportMapFragment
                mGoogleMap = fm.getMap();
                mGoogleMap.setMyLocationEnabled(true);
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
                //new GetUserUpdates().execute(sendName);
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }

            drawMarker(receiverLat,receiverLong,sendLat,sendLong,eveLat,eveLong);

        }  // End of else

    }  // End of onCreate

    //Method onLocationChanged is called when the location is updated.
    @Override
    public void onLocationChanged( Location location ) {
        mGoogleMap.clear();
        // marker.remove();
        //TextView locationTv = (TextView) findViewById( R.id.latlongLocation );
        String Latitude_Holder     = Double.toString(location.getLatitude( ));
        String Longitude_Holder    = Double.toString(location.getLongitude( ));
        Log.i("hello1", "onLocationChanged: "+Latitude_Holder);

        if((Latitude_Holder.equalsIgnoreCase(Double.toString(receiverLat))) || (Longitude_Holder.equalsIgnoreCase(Double.toString(receiverLong))))
        {
            //Do Nothing.
        }
        else {
            receiverLat = Double.parseDouble(Latitude_Holder);
            receiverLong = Double.parseDouble(Longitude_Holder);
            Log.i("hello2", "onLocationChanged: got latitude");
        }
        drawMarker(receiverLat,receiverLong,sendLat,sendLong,eveLat,eveLong);


    }

    public void drawMarker(Double receiverLat,Double receiverLong,Double sendLat,Double sendLong,Double eveLat,Double eveLong)
    {
        marker1 = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(sendLat,sendLong)).title(sendName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        marker2 = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(receiverLat,receiverLong)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        marker3 = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(eveLat,eveLong)).title(eveName).snippet("Event Location: "+eveLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        user_origin =  new LatLng(receiverLat,receiverLong);
        sender_origin = new LatLng(sendLat,sendLong);
        destination = new LatLng(eveLat,eveLong);
        // Building the URL including Google Directions API
        url = getDirectionsUrl( user_origin, destination );
        url1 = getDirectionsUrl( sender_origin,destination);
        DownloadTask downloadTask = new DownloadTask( );
        // Start downloading JSON data from Google Directions API.
        downloadTask.execute( url );
        DownloadTask downloadTask1 = new DownloadTask( );
        // Start downloading JSON data from Google Directions API.
        downloadTask1.execute( url1 );
    }


    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            mGoogleMap.clear();
            new GetUserUpdates().execute(sendName);
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
            Log.i("run", "run: "+sendLat);
            handler.postDelayed(updateTimerThread,4000);
        }
    };

    class GetUserUpdates extends AsyncTask<String,Void,String>
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
                String data = URLEncoder.encode("name_sender", "UTF-8") + "=" + URLEncoder.encode(sendName, "UTF-8");
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
                    new_send_lat = Double.parseDouble(jsonObject.getString("latofuser"));
                    new_send_long = Double.parseDouble(jsonObject.getString("longofuser"));
                }
                if((new_send_lat == sendLat) && (new_send_long == sendLong))
                {
                    //do Nothing
                }
                else
                {
                    sendLat = new_send_lat;
                    sendLong = new_send_long;
                }
                drawMarker(receiverLat,receiverLong,sendLat,sendLong,eveLat,eveLong);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            // adapter1.notifyDataSetChanged();
        }
    }


    private String getDirectionsUrl( LatLng origin, LatLng dest ) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest  = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the URL for the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }  // End of getDirectionsUrl


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
                //data += downloadUrl(url[1]);
            }
            catch( Exception e ) {
                Log.d( "Background Task", e.toString( ) );
            }
            Log.i("data getting printed: ", ":"+data);
            return data;
        }  // End of doInBackground


        // Executes in UI thread, after the execution of doInBackground( )
        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            ParserTask parserTask = new ParserTask( );
            // Invokes the thread for parsing the JSON data.
            parserTask.execute( result );
        }  // End of onPostExecute

    }  // End of DownloadTask


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
            return routes;
        }  // End of doInBackground


        // Executes in UI thread, after the parsing process.
        @Override
        protected void onPostExecute(
                List<List<HashMap<String, String>>> result ) {
            ArrayList<LatLng> points = null;
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
                    double lng = Double.parseDouble( point.get( "lng" ) );
                    LatLng position = new LatLng( lat, lng );
                    points.add( position );
                }  // End of inner for

                // Adding all the points in the route to LineOptions
                lineOptions.addAll( points );
                lineOptions.width( 2 );
                lineOptions.color( Color.RED );
            }  // End of outer for

            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline( lineOptions );
        }  // End of onPostExecute

    }  // End of ParserTask

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

}  // End of MainActivity


