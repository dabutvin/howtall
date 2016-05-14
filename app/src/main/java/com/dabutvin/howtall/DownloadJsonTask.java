package com.dabutvin.howtall;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadJsonTask extends AsyncTask<String, Void, String> {
    private final StringCallbackInterface callback;

    public DownloadJsonTask(StringCallbackInterface callback){
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... url) {
        HttpURLConnection urlConnection = null;
        String error = null;

        Log.d("DOWNLOAD", url[0]);
        try {
            urlConnection = (HttpURLConnection) new URL(url[0]).openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null){
                result.append(line);
            }

            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            error = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            error = e.getMessage();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return error;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onTaskFinished(result);
    }
}