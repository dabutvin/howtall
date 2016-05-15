package com.dabutvin.howtall;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 5/14/2016.
 */
public class Person {
    private String name;
    private int height;
    private String image;

    public static Person ToPerson(JSONObject jsonObject){
        Person person = new Person();

        try{
            person.setName(jsonObject.getString("name"));
            person.setHeight(jsonObject.getInt("height"));
            person.setImage(jsonObject.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
