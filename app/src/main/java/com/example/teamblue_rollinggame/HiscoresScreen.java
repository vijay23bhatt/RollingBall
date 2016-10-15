package com.example.teamblue_rollinggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HiscoresScreen extends AppCompatActivity {
    String scoreKey = "com.example.teamblue_rollinggame.score";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiscores_screen);
        SharedPreferences shared = this.getSharedPreferences("com.example.teamblue_rollinggame", Context.MODE_PRIVATE);
        Set<String> scores = shared.getStringSet(scoreKey, null);
        TextView scoresText = (TextView) findViewById(R.id.hiscore_Text);
        if(scores!=null ){
            if(scores.size() == 0){
                scoresText.setText("Empty hi-scores!");
            }
            else {
                String scoresString = "";

                List<Integer> sortedList = new ArrayList<Integer>();
                for (String s : scores) {
                    Integer test = null;
                    try {
                        test = Integer.parseInt(s);
                    } catch (NumberFormatException e) {

                    }
                    if (test != null) {
                        sortedList.add(Integer.parseInt(s));
                    }
                }
                Collections.sort(sortedList);
                Collections.reverse(sortedList);
                int count = 1;
                for (Integer n : sortedList) {
                    scoresString += "\n" + count + ": " + n;
                    count++;
                }
                scoresText.setText(scoresString);
            }
        }
        else{
            scoresText.setText("Empty hi-scores!");
        }

    }

    public void clearScores(View view){
        SharedPreferences shared = this.getSharedPreferences("com.example.teamblue_rollinggame", Context.MODE_PRIVATE);
        Set<String> scores = shared.getStringSet(scoreKey, null);
        scores.clear();
        shared.edit().putStringSet(scoreKey, scores).apply();
        shared.edit().remove(scoreKey);
        shared.edit().clear();
        shared.edit().apply();
        TextView scoresText = (TextView)findViewById(R.id.hiscore_Text);
        scoresText.setText("Empty hi-scores!");
    }

    public void back(View view){
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
