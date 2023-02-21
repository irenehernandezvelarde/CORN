package com.example.cornapp;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class UtilsHTTP {
        public static void sendGET(String url, Consumer<String> callBack) {
            send("GET", url, "", callBack);
        }

        public static void sendPOST(String url, String post_params, Consumer<String> callBack) {
            send("POST", url, post_params, callBack);
        }

        private static void send(String type, String url, String post_params, Consumer<String> callBack) {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            executor.execute(new Runnable() {
                @Override
                public void run() {

                    // Tasques en background (xarxa)
                    try {
                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod(type);
                        con.setRequestProperty("User-Agent", "Mozilla/5.0");

                        if (type.equals("POST")) {
                            con.setDoOutput(true);
                            OutputStream os = con.getOutputStream();
                            os.write(post_params.getBytes());
                            os.flush();
                            os.close();
                        }

                        int responseCode = con.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) { //success
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            //return response.toString();
                            callBack.accept(response.toString());
                        } else {
                            System.out.println(type + " request did not work.");
                        }
                    } catch (Exception e) {
                        System.out.println(type + " request error.");
                        System.out.println(e);

                    }
                    //return "{ \"status\": \"KO\", \"result\": \"Error on " + type + " request\" }";
                    callBack.accept("{ \"status\": \"KO\", \"result\": \"Error on " + type + " request\" }");

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            // Tasques a la interfície gràfica (GUI)

                        }
                    });
                }
            });            // Create a new thread to send the request


        }
}



