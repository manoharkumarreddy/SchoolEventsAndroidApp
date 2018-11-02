package com.example.manoh.project515;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manoh on 4/10/2018.
 */

public class AdapterUserRequestReceivedProcessActivity extends RecyclerView.Adapter<AdapterUserRequestReceivedProcessActivity.AdapterUserRequestReceivedProcessActivityViewHolder> {

    //Store all the data required in a list.
    private Context mContext;
    private ArrayList<EventDataAdapter> eventDataList2;
    //HashMap<String,Double> user_eve_lat_long = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String result;
    String HttpURL = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UpdateRequestStatus.php";
    //Getting both context and list with constructor.
    public AdapterUserRequestReceivedProcessActivity(Context context,ArrayList<EventDataAdapter> eventDataList)
    {
        this.eventDataList2 = eventDataList;
        this.mContext = context;
    }

    @Override
    public AdapterUserRequestReceivedProcessActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_requests_received, parent,false);
        return new AdapterUserRequestReceivedProcessActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterUserRequestReceivedProcessActivityViewHolder holder, int position)
    {
        //getting the data of events of the specified position
        final EventDataAdapter eventDataAdapter = eventDataList2.get(position);
        //Binding the data with view holder views
        holder.Holds_Event_Name1.setText(eventDataAdapter.getEName1());
        holder.Holds_Sender_Name1.setText(eventDataAdapter.getUNameSender1());
        holder.Holds_Event_Desc1.setText(eventDataAdapter.getEDesc());
        holder.Holds_Event_Loc1.setText(eventDataAdapter.getELoc());
        holder.Holds_Event_Date1.setText(eventDataAdapter.getEDate());
        holder.Holds_Event_Time1.setText(eventDataAdapter.getETime());
        if(eventDataAdapter.getRStatus().equalsIgnoreCase("0"))
        {
            holder.Holds_Rejects1.setVisibility(View.INVISIBLE);
            holder.Holds_Acceptance1.setVisibility(View.INVISIBLE);
            holder.Holds_Directions1.setVisibility(View.VISIBLE);
        }
        else if(eventDataAdapter.getRStatus().equalsIgnoreCase("2"))
        {
            holder.Holds_Rejects1.setVisibility(View.INVISIBLE);
            holder.Holds_Acceptance1.setVisibility(View.INVISIBLE);
            holder.Holds_Directions1.setVisibility(View.INVISIBLE);
        }

      /*holder.Holds_Rejects1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status_accept = 0;
                String HttpURL_accept = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UpdateRequestStatus.php";
                user_status_update(eventDataAdapter.getUNameReceiver1(),eventDataAdapter.getEName1(),eventDataAdapter.getUNameSender(),status_accept,HttpURL_accept);
                holder.Holds_Rejects1.setVisibility(View.INVISIBLE);
                holder.Holds_Acceptance1.setVisibility(View.INVISIBLE);
                holder.Holds_Directions1.setVisibility(View.VISIBLE);
            }
        });

        holder.Holds_Acceptance1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status_accept = 2;
                String HttpURL_accept = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UpdateRequestStatus.php";
                user_status_update(eventDataAdapter.getUNameReceiver1(),eventDataAdapter.getEName1(),eventDataAdapter.getUNameSender(),status_accept,HttpURL_accept);
                holder.Holds_Rejects1.setVisibility(View.INVISIBLE);
                holder.Holds_Acceptance1.setVisibility(View.INVISIBLE);
                holder.Holds_Directions1.setVisibility(View.VISIBLE);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        //returning the siz eof the list.
        int totalsize = 0;
        if(eventDataList2.size() != 0)
            totalsize = eventDataList2.size();
        return totalsize;
    }


    class User_Status_Update extends AsyncTask<String,Void,String>
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
               Log.i("Final Answer: ", invitation_answer);
              // Toast.makeText(UserActivity.this,invitation_answer,Toast.LENGTH_LONG).show();
           }

           @Override
           protected String doInBackground(String... params) {
               HashMap<String,String> hashMap1 = new HashMap<String, String>();
               hashMap1.put("u_sender",params[0]);
               hashMap1.put("e_name",params[1]);
               hashMap1.put("u_receiver",params[2]);
               hashMap1.put("r_status",params[3]);
               result = httpParse.postRequest(hashMap1,HttpURL);
               return result;
           }
       }


    class AdapterUserRequestReceivedProcessActivityViewHolder extends RecyclerView.ViewHolder{

        public TextView Holds_Sender_Name1, Holds_Event_Name1, Holds_Event_Desc1, Holds_Event_Loc1, Holds_Event_Date1, Holds_Event_Time1, Holds_Acceptance1, Holds_Rejects1, Holds_Directions1;

        public AdapterUserRequestReceivedProcessActivityViewHolder(View itemView) {
            super(itemView);

            Holds_Sender_Name1 = itemView.findViewById(R.id.NameOfSender1Value);
            Holds_Event_Name1 = itemView.findViewById(R.id.NameOfEventSent1Value);
            Holds_Event_Desc1 = itemView.findViewById(R.id.DescOfEventSent1Value);
            Holds_Event_Loc1 = itemView.findViewById(R.id.LocOfEventSent1Value);
            Holds_Event_Date1 = itemView.findViewById(R.id.DateOfEventSent1Value);
            Holds_Event_Time1 = itemView.findViewById(R.id.TimeOfEventSent1Value);
            Holds_Acceptance1 = itemView.findViewById(R.id.RequestAccept);
            Holds_Rejects1 = itemView.findViewById(R.id.Reject);
            Holds_Directions1 = itemView.findViewById(R.id.GetDirections);


            Holds_Rejects1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    //UserRequestReceivedProcessActivity(mcxt)).user_status_update(eventDataList2.get(getAdapterPosition()));
                    //String status_reject = "2";
                    //String HttpURL_accept = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UpdateRequestStatus.php";
                    EventDataAdapter adapterdata = eventDataList2.get(pos);
                    // user_status_update(adapterdata.getUNameReceiver1(),adapterdata.getEName1(),adapterdata.getUNameSender(),status_reject);
                    User_Status_Update status_update = new User_Status_Update();
                    status_update.execute(adapterdata.getUNameSender(), adapterdata.getEName1(), adapterdata.getUNameReceiver1(), adapterdata.getRStatus2());
                    Holds_Rejects1.setVisibility(View.INVISIBLE);
                    Holds_Acceptance1.setVisibility(View.INVISIBLE);
                    //Holds_Directions1.setVisibility(View.VISIBLE);
                }
            });

            Holds_Acceptance1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //String status_accept = "0";
                    int pos = getAdapterPosition();
                    //String HttpURL_reject = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UpdateRequestStatus.php";
                    EventDataAdapter adapterdata = eventDataList2.get(pos);
                    //user_status_update(adapterdata.getUNameReceiver1(), adapterdata.getEName1(), adapterdata.getUNameSender(), status_accept);
                    User_Status_Update status_update = new User_Status_Update();
                    Log.i("Data being sent: ", adapterdata.getUNameSender() + ":" + adapterdata.getEName1() + ":" + adapterdata.getUNameReceiver1() + ":" + adapterdata.getRStatus1());
                    status_update.execute(adapterdata.getUNameSender(), adapterdata.getEName1(), adapterdata.getUNameReceiver1(), adapterdata.getRStatus1());
                    Holds_Rejects1.setVisibility(View.INVISIBLE);
                    Holds_Acceptance1.setVisibility(View.INVISIBLE);
                    Holds_Directions1.setVisibility(View.VISIBLE);
                }
            });

            Holds_Directions1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    //UserRequestReceivedProcessActivity(mcxt)).user_status_update(eventDataList2.get(getAdapterPosition()));
                    //String status_reject = "2";
                    //String HttpURL_accept = "http://undcemcs02.und.edu/~manoharkumarreddy.da/UpdateRequestStatus.php";
                    EventDataAdapter adapterdata = eventDataList2.get(pos);
                    //double senderLatitude = Double.parseDouble(adapterdata.getSenderLat());
                    //user_eve_lat_long.put("senderlat",senderLatitude);
                    //double senderLongitude = Double.parseDouble(adapterdata.getSenderLong());
                    //user_eve_lat_long.put("senderlong",senderLongitude);
                    //double receiverLatitude = Double.parseDouble(adapterdata.getReceiverLat());
                    //user_eve_lat_long.put("receiverlat",receiverLatitude);
                    //double receiverLongitude = Double.parseDouble(adapterdata.getReceiverLong());
                    //user_eve_lat_long.put("receiverlong",receiverLongitude);
                    //double eventLatitude = Double.parseDouble(adapterdata.getELat());
                    //user_eve_lat_long.put("evelat",eventLatitude);
                    //double eventLongitude = Double.parseDouble(adapterdata.getELong());
                    //user_eve_lat_long.put("evelong",eventLongitude);
                    Intent intent = new Intent(mContext,UserDirectionActivity.class);
                    intent.putExtra("sendername",adapterdata.getUNameSender1());
                    intent.putExtra("eventname",adapterdata.getEName1());
                    intent.putExtra("eventlocation",adapterdata.getELoc());
                    intent.putExtra("reclat",adapterdata.getReceiverLat());
                    intent.putExtra("reclong",adapterdata.getReceiverLong());
                    intent.putExtra("senderlat",adapterdata.getSenderLat());
                    intent.putExtra("senderlong",adapterdata.getSenderLong());
                    intent.putExtra("evelat",adapterdata.getELat());
                    intent.putExtra("evelong",adapterdata.getELong());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    //Log.i("lat and long data:", receiverLatitude+" : "+receiverLongitude+" : "+eventLatitude+" : "+eventLongitude);
                    //LatLng origin = new LatLng(receiverLatitude, receiverLongitude);
                    //LatLng destination = new LatLng(eventLatitude, eventLongitude);
                    //Building the URL including Google Firection API
                    //String url = getDirectionsUrl(origin, destination);
                    //new DownloadTask().execute(url);
                    // user_status_update(adapterdata.getUNameReceiver1(),adapterdata.getEName1(),adapterdata.getUNameSender(),status_reject);
                    //User_Status_Update status_update = new User_Status_Update();
                    //status_update.execute(adapterdata.getUNameSender(),adapterdata.getEName1(),adapterdata.getUNameReceiver1(),adapterdata.getRStatus2());
                    //Holds_Rejects1.setVisibility(View.INVISIBLE);
                    //Holds_Acceptance1.setVisibility(View.INVISIBLE);
                    //Holds_Directions1.setVisibility(View.VISIBLE);
                }
            });
        }
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
            //mgoogleMap.addPolyline( lineOptions );
        }  // End of onPostExecute

    }  // End of ParserTask*/


}

