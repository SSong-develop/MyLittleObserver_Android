package com.example.mylittleobserver_android.ServerConnect;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getFiles extends Activity {
    public static String getJson(String serverUrl, String postParams) throws Exception {
        
        StringBuilder sb = null;
        BufferedReader bufferedReader = null;
        try{
            Thread.sleep(100);
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            sb = new StringBuilder();

            if(connection != null) {
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT","Mozilla/5.0");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.setRequestProperty("Accept-Language","en-US,en;q=0.5");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeByte(Integer.parseInt(postParams));
                wr.flush();
                wr.close();

                int responseCode = connection.getResponseCode();
                System.out.println("GET Response COde :"+ responseCode);
                if(responseCode == HttpURLConnection.HTTP_OK){
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+'\n');
                    }
                }
                bufferedReader.close();
            }
            return sb.toString().trim();
        } catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString().trim();
    }
}
