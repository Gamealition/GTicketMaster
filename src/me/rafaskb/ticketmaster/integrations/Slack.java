package me.rafaskb.ticketmaster.integrations;

import me.rafaskb.ticketmaster.TicketMaster;
import me.rafaskb.ticketmaster.utils.ConfigLoader;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class Slack {


    public static void sendMessage(String message, String username, String world, double x , double z){

        URL url;
        try {
            url = new URL(ConfigLoader.getSlackwebhookurl());

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("payload", generateMessage(message,username,world,x,z) );

        StringBuilder postData = new StringBuilder();
        try {
            for (Map.Entry<String, Object> param : params.entrySet()) {

                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String response = "";
            for ( int c = in.read(); c != -1; c = in.read() ) {
                response +=(char) c;
            }

            TicketMaster.getInstance().getLogger().info("message from Slack server: "+response);

        } catch (IOException e) {
            TicketMaster.getInstance().getLogger().warning("Ticket Master Slack integration IO error:"+e.getMessage());
        }
        } catch (MalformedURLException e) {
            TicketMaster.getInstance().getLogger().warning("Ticket Master Slack integration URL error:" +e.getMessage());
        }

    }


    private static String generateMessage(String message, String username, String world, double x , double z){

        String m = "{";
        m+= "\"text\":\""+message+" "+generateDynmapURL(world,x,z)+"\",";
        m+= "\"username\":\""+username+"\",";
        m+= "\"icon_url\":\"https://minotar.net/avatar/"+username+"/100.png\"";
        m+= "}";

        System.out.println(m);
        return m;
    }

    private static String generateDynmapURL(String world, double x , double z){
        String dynmap = "http://gamealition.com:8123";
        //check for dynmap link 3 is an arbitary number bad bad me.
        if(ConfigLoader.getDynmapurl().length()>3){
            return "<"+dynmap+"/?worldname="+world+"&mapname=flat&zoom=6&x="+x+"&y=64&z="+z+ "|Dynmap Link>";
        }
        return "";


    }


}
