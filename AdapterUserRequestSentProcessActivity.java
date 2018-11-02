package com.example.manoh.project515;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoh on 4/10/2018.
 */

public class AdapterUserRequestSentProcessActivity extends RecyclerView.Adapter<AdapterUserRequestSentProcessActivity.AdapterUserRequestSentProcessActivityViewHolder> {

    //this is the context which we use to inflate the layout.
    private Context mCtx;

    //Store all the data required in a list.
    private ArrayList<EventDataAdapter> eventDataList;

    //Getting both context and list with constructor.
    public AdapterUserRequestSentProcessActivity(Context context,ArrayList<EventDataAdapter> eventDataList)
    {
        this.eventDataList = eventDataList;
        this.mCtx = context;
    }

    @Override
    public AdapterUserRequestSentProcessActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_requests_sent, parent,false);
        return new AdapterUserRequestSentProcessActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterUserRequestSentProcessActivityViewHolder holder, int position)
    {
        //getting the data of events of the specified position
        EventDataAdapter eventDataAdapter = eventDataList.get(position);

        //Binding the data with view holder views
        holder.Holds_Event_Name.setText(eventDataAdapter.getEName());
        holder.Holds_Receiver_Name.setText(eventDataAdapter.getUNameReceiver());
        holder.Holds_Request_Status.setText(eventDataAdapter.getRStatus());

        if(eventDataAdapter.getRStatus().equalsIgnoreCase("Accepted"))
        {
            holder.Holds_Directions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        //returning the siz eof the list.
        int totalsize = 0;
        if(eventDataList.size() != 0)
            totalsize = eventDataList.size();
        return totalsize;
    }


    class AdapterUserRequestSentProcessActivityViewHolder extends RecyclerView.ViewHolder{

        public TextView Holds_Receiver_Name, Holds_Event_Name, Holds_Request_Status, Holds_Directions;

        public AdapterUserRequestSentProcessActivityViewHolder(View itemView)
        {
            super(itemView);

            Holds_Receiver_Name = itemView.findViewById(R.id.NameOfReceiverValue);
            Holds_Event_Name = itemView.findViewById(R.id.NameOfEventSentValue);
            Holds_Request_Status = itemView.findViewById(R.id.SentRequestStatusValue);
            Holds_Directions = itemView.findViewById(R.id.GettingDirections);

            Holds_Directions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    EventDataAdapter adapterdata = eventDataList.get(pos);

                    Intent intent = new Intent(mCtx,UserDirectionActivity.class);
                    intent.putExtra("sendername",adapterdata.getUNameReceiver());
                    intent.putExtra("eventname",adapterdata.getEName());
                    intent.putExtra("eventlocation",adapterdata.getELoc1());
                    intent.putExtra("reclat",adapterdata.getSenderLat1());
                    intent.putExtra("reclong",adapterdata.getSenderLong1());
                    intent.putExtra("senderlat",adapterdata.getReceiverLat1());
                    intent.putExtra("senderlong",adapterdata.getReceiverLong1());
                    intent.putExtra("evelat",adapterdata.getELat1());
                    intent.putExtra("evelong",adapterdata.getELong1());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mCtx.startActivity(intent);
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
}
