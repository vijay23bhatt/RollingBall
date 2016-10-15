package com.example.teamblue_rollinggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    String scoreKey = "com.example.teamblue_rollinggame.score";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void optionSelect(View view){
    Intent intent;
        intent = new Intent(this, OptionsScreen.class);
        startActivity(intent);
        finish();
    }

    public void hiscoreSelect(View view){
        Intent intent;
        intent = new Intent(this, HiscoresScreen.class);
        startActivity(intent);
        finish();
    }

    public void gameSelect(View view){
        Intent intent;
        intent = new Intent(this, GameScreen.class);
        startActivity(intent);

    }

    public void exit(View view){
        finish();
    }
}
