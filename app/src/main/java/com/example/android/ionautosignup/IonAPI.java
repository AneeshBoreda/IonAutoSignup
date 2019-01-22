package com.example.android.ionautosignup;

import android.annotation.TargetApi;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

public class IonAPI {
    private String user;
    private String pass;
    public static String SIGNUPS="https://ion.tjhsst.edu/api/signups/user";
    public static String BLOCKS="https://ion.tjhsst.edu/api/blocks";
    public JSONArray signups;
    public String dataStr;
    public JSONArray allBlocks;
    public JSONArray allActivities;
    public IonAPI(String username, String password)
    {
        this.user=username;
        this.pass=password;
    }
    public JSONArray getAllSignups()
    {

        try {
            signups=new JSONArray(get(this.SIGNUPS));
            return signups;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public JSONObject updateBlocks()
    {
        try {
            JSONObject blocks=new JSONObject(get(this.BLOCKS));
            return blocks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @TargetApi(26)
    public String get(String urlinput)
    {
        try {
            URL url = new URL(urlinput);
            String encoding = Base64.getEncoder().encodeToString((this.user + ":" + this.pass).getBytes(StandardCharsets.UTF_8));
            System.out.println(encoding);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            //System.out.println(connection.getResponseCode());
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(content));
            String line;
            String ret="";

            while ((line = in.readLine()) != null) {

                ret+=line;
            }
            System.out.println(ret);

            return ret;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    @TargetApi(26)
    public String signUp(int block,int activityID,int scheduledActivity)
    {
        try {
            URL url = new URL(SIGNUPS);
            String encoding = Base64.getEncoder().encodeToString((this.user + ":" + this.pass).getBytes(StandardCharsets.UTF_8));
            System.out.println(encoding);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("Accept","text/html");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.connect();
            //System.out.println(connection.getResponseCode());
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("block", block);
            jsonParam.put("activity",activityID );
            jsonParam.put("scheduled_activity",scheduledActivity);
            jsonParam.put("use_scheduled_activity",true);
            jsonParam.put("force",false);


            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(jsonParam.toString());


            os.flush();
            os.close();
            Log.i("STATUS", String.valueOf(connection.getResponseCode()));
            Log.i("MSG" , connection.getResponseMessage());
            Log.i("json",jsonParam.toString());
            String str=connection.getResponseMessage();
            connection.disconnect();
            return connection.getResponseCode()+"";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    @TargetApi(26)
    public JSONArray getAllActivities()
    {
        try {


            String temp = get(this.BLOCKS);
            allBlocks=new JSONArray();

            JSONObject jsonData = new JSONObject(temp);
            JSONArray tmpArr=jsonData.getJSONArray("results");
            for(int i=0; i<tmpArr.length();i++)
            {
                JSONObject entry=tmpArr.getJSONObject(i);
                if(!entry.getBoolean("locked"))
                {
                    JSONObject activity=new JSONObject();
                    activity.put("id",entry.get("id"));
                    activity.put("url",entry.get("url"));
                    activity.put("date",LocalDate.parse((String)entry.get("date")).getDayOfWeek());
                    activity.put("block_letter",entry.get("block_letter"));
                    allBlocks.put(activity);
                }
            }
            String nextUrl=jsonData.getString("next");
            while(!nextUrl.trim().equals("null"))
            {
                String nextData=get(nextUrl);
                //System.out.println(nextUrl);
                jsonData = new JSONObject(nextData);
                tmpArr=jsonData.getJSONArray("results");
                for(int i=0; i<tmpArr.length();i++)
                {
                    JSONObject entry=tmpArr.getJSONObject(i);
                    //System.out.println(entry.getBoolean("locked"));
                    if(!entry.getBoolean("locked"))
                    {
                        System.out.println("Adding entry");
                        JSONObject activity=new JSONObject();
                        activity.put("id",entry.get("id"));
                        activity.put("url",entry.get("url"));
                        activity.put("date",LocalDate.parse((String)entry.get("date")).getDayOfWeek());
                        activity.put("block_letter",entry.get("block_letter"));
                        allBlocks.put(activity);
                    }
                }
                //System.out.println("finished iteration");
                //System.out.println(jsonData.getString("next"));
                nextUrl=jsonData.getString("next");
                //System.out.println(nextUrl.trim().equals("null"));

            }
            System.out.println("All Blocks");
            System.out.println(allBlocks);
            return allBlocks;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject getAllActivitiesinBlock(int blockID)
    {
        try {
            String result = get(this.BLOCKS + "/" + blockID);
            JSONObject json = new JSONObject(result);
            return json;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    public HashMap<String, Integer> getActivitiesList(int blockID)
    {
        try {
            HashMap<String, Integer> result = new HashMap<>();
            JSONObject activities = getAllActivitiesinBlock(blockID).getJSONObject("activities");
            Iterator<String> iter=activities.keys();
            while(iter.hasNext())
            {
                String key=iter.next();
                String name=activities.getJSONObject(key).getString("name");
                int id=activities.getJSONObject(key).getInt("aid");
                result.put(name,id);
            }
            return result;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
