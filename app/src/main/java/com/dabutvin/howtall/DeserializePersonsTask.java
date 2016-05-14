package com.dabutvin.howtall;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 5/14/2016.
 */
public class DeserializePersonsTask extends AsyncTask<String, Void, List<Person>> {
    private PersonsCallbackInterface callback;

    DeserializePersonsTask(PersonsCallbackInterface callback){

        this.callback = callback;
    }

    @Override
    protected List<Person> doInBackground(String... params) {
        String json = params[0];

        List<Person> persons = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonPersonsArray = jsonObject.getJSONArray("persons");
            for(int i=0; i<jsonPersonsArray.length(); i++) {
                JSONObject jsonPersonObject = jsonPersonsArray.getJSONObject(i);
                persons.add(Person.ToPerson(jsonPersonObject));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return persons;
    }

    @Override
    protected void onPostExecute(List<Person> result) {
        Log.d("Deserializer", "Deserialized " + result.size() + " persons");
        callback.onTaskFinished(result);
    }
}
