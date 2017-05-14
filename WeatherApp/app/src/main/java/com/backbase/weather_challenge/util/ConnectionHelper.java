package com.backbase.weather_challenge.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Helper to perform all the connections to get the weather information from http://openweathermap.org/
 */
public class ConnectionHelper {
    private static final String TAG = ConnectionHelper.class.getSimpleName();
    private static final int TIME_OUT_CONNECTION = 7000;
    private static final String WEATHER_SERVICE_API_KEY = "c6e381d8c7ff98f0fee43775817cf6ad";
    private static final String WEATHER_SERVICE_URL = "http://api.openweathermap.org/data/2.5/weather?appid=%s&lat=%s&lon=%s&units=metric";

    public ConnectionHelper() {
        super();
    }

    public String getWeatherServiceInfo(double latitude, double longitude, String type) {
        String serverResponse = null;
        try {
            String weatherService = String.format(WEATHER_SERVICE_URL, WEATHER_SERVICE_API_KEY, latitude, longitude);
            HttpURLConnection connection = getHttpsURLConnection(weatherService);
            connection.setConnectTimeout(TIME_OUT_CONNECTION);
            connection.setReadTimeout(TIME_OUT_CONNECTION);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("appid", WEATHER_SERVICE_API_KEY);
            connection.setRequestProperty("lat", String.valueOf(latitude));
            connection.setRequestProperty("lon", String.valueOf(longitude));
            connection.setRequestProperty("units", "metric");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                serverResponse = new String(readBytes(in));
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private HttpURLConnection getHttpsURLConnection(String serviceUrl) {
        try {
            java.net.URL url = new URL(serviceUrl);
            if (url.getProtocol().equals("https")) {
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                return urlConnection;
            } else {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                return urlConnection;
            }
        } catch (Exception e) {
            Log.e(TAG, "URL connection error:" + e.getMessage(), e);
            return null;
        }

    }


}
