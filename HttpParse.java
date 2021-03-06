package com.example.manoh.project515;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manoh on 3/22/2018.
 */

public class HttpParse {

    // Declaring variables needed.
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    String FinalHttpData = "";
    StringBuilder stringBuilder = new StringBuilder();
    String Result;

    // POST request method
    public String postRequest(HashMap<String,String> Data, String HttpUrlHolder)
    {
        try
        {
            url = new URL(HttpUrlHolder);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(14000);
            httpURLConnection.setConnectTimeout(14000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            outputStream = httpURLConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            bufferedWriter.write(FinalDataParse(Data));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                FinalHttpData = bufferedReader.readLine();
            }
            else {
                FinalHttpData = "Something went wrong";
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //Log.d("Data returned: ", FinalHttpData);
        return FinalHttpData;
    }

    public String FinalDataParse(HashMap<String,String> hashMap2) throws UnsupportedEncodingException
    {
        for(Map.Entry<String,String> map_entry : hashMap2.entrySet())
        {
            stringBuilder.append("&");
            stringBuilder.append(URLEncoder.encode(map_entry.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(map_entry.getValue(),"UTF-8"));
        }
        Result = stringBuilder.toString();
        return Result;
    }
}
