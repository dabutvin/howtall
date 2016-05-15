package com.dabutvin.howtall;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSeekBarChangeListener, ViewSwitcher.ViewFactory {

    List<Person> persons = new ArrayList<>();
    int personindex = 0;
    int selectedHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((SeekBar)findViewById(R.id.seekbar)).setOnSeekBarChangeListener(this);
        TextSwitcher answer = ((TextSwitcher)findViewById(R.id.answer));
        answer.setFactory(this);
        answer.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));


        new DownloadJsonTask(new StringCallbackInterface() {
            @Override
            public void onTaskFinished(String json) {
                new DeserializePersonsTask(new PersonsCallbackInterface() {
                    @Override
                    public void onTaskFinished(List<Person> result) {
                        for (int i=0; i<result.size(); i++) {
                            persons.add(result.get(i));
                        }

                        startNewRound();
                    }
                }).execute(json);
            }
        }).execute("http://howtall.azurewebsites.net/api/");
    }

    private void startNewRound(){
        Person person = persons.get(personindex);

        ((TextView)findViewById(R.id.name)).setText(person.getName());
        Picasso.with(this).load(person.getImage()).into((ImageView) findViewById(R.id.img));
        ((SeekBar)findViewById(R.id.seekbar)).setProgress(0);
        ((TextSwitcher)findViewById(R.id.answer)).setCurrentText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void skip(View view) {
        if (persons.size() == personindex + 1) {
            personindex = 0;
        } else {
            personindex++;
        }

        startNewRound();
    }

    public void submit(View view) {
        TextSwitcher t = ((TextSwitcher) findViewById(R.id.answer));

        if (persons.size() > personindex && selectedHeight == persons.get(personindex).getHeight()) {
            ((TextView)t.getChildAt(0)).setTextColor(Color.GREEN);
        } else {
            ((TextView)t.getChildAt(0)).setTextColor(Color.RED);
        }

        t.setText(formatInches(persons.get(personindex).getHeight()));
    }

    private String formatInches(int totalInches) {
        int feet = totalInches / 12;
        int inches = totalInches % 12;

        return "" + feet + " ft " + String.format("%02d", inches) + " in";
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        selectedHeight = (progress / 2)  + 48;
        ((TextView)findViewById(R.id.selectedHeight)).setText(formatInches(selectedHeight));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public View makeView() {
        TextView t = new TextView(this);

        t.setGravity(Gravity.CENTER_HORIZONTAL);
        t.setTextSize(42);
        return t;
    }
}
