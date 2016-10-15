package com.example.teamblue_rollinggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class OptionsScreen extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context mContext;
    Spinner spinnerSpeed;

    ImageButton btnBBlue, btnBRed, btnBGreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_screen);
        mContext = OptionsScreen.this;
        btnBBlue = (ImageButton)findViewById(R.id.imgbtnBlue);
        btnBBlue.setOnClickListener(this);
        btnBRed = (ImageButton)findViewById(R.id.imgbtnRed);
        btnBRed.setOnClickListener(this);
        btnBGreen = (ImageButton)findViewById(R.id.imgbtnGreen);
        btnBGreen.setOnClickListener(this);

        spinnerSpeed = (Spinner)findViewById(R.id.spinnerSpeed);

        sharedPref = getSharedPreferences(com.example.teamblue_rollinggame.Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        spinnerSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerSpeed.getSelectedItemPosition() == 0) {
                    editor = sharedPref.edit();
                    editor.putString(Config.KEY_SPEED, "slow");
                    editor.commit();
                }
                else if (spinnerSpeed.getSelectedItemPosition() == 1)
                {
                    editor = sharedPref.edit();
                    editor.putString(Config.KEY_SPEED, "medium");
                    editor.commit();
                }
                else if (spinnerSpeed.getSelectedItemPosition() == 2)
                {
                    editor = sharedPref.edit();
                    editor.putString(Config.KEY_SPEED, "fast");
                    editor.commit();
                }
                Log.d("CHECKER",sharedPref.getString(Config.KEY_SPEED,""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String color = sharedPref.getString(Config.KEY_COLOR, "");
        if(!color.equals("")){
            if(color.equals("blue")){
                btnBBlue.setBackgroundColor(Color.BLACK);
                btnBRed.setBackgroundColor(Color.WHITE);
                btnBGreen.setBackgroundColor(Color.WHITE);
            }
            else if(color.equals("red")){
                btnBBlue.setBackgroundColor(Color.WHITE);
                btnBRed.setBackgroundColor(Color.BLACK);
                btnBGreen.setBackgroundColor(Color.WHITE);
            }
            else if(color.equals("green")){
                btnBBlue.setBackgroundColor(Color.WHITE);
                btnBRed.setBackgroundColor(Color.WHITE);
                btnBGreen.setBackgroundColor(Color.BLACK);
            }
        }

    }

    public void back(View view){
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int v = view.getId();
        editor = sharedPref.edit();
        switch(v){
            case R.id.imgbtnBlue:
                editor.putString(Config.KEY_COLOR, "blue");
                editor.commit();
                btnBBlue.setBackgroundColor(Color.BLACK);
                btnBRed.setBackgroundColor(Color.WHITE);
                btnBGreen.setBackgroundColor(Color.WHITE);
                break;
            case R.id.imgbtnRed:
                editor.putString(Config.KEY_COLOR, "red");
                editor.commit();
                btnBBlue.setBackgroundColor(Color.WHITE);
                btnBRed.setBackgroundColor(Color.BLACK);
                btnBGreen.setBackgroundColor(Color.WHITE);
                break;
            case R.id.imgbtnGreen:
                editor.putString(Config.KEY_COLOR, "green");
                editor.commit();
                btnBBlue.setBackgroundColor(Color.WHITE);
                btnBRed.setBackgroundColor(Color.WHITE);
                btnBGreen.setBackgroundColor(Color.BLACK);
                break;
        }

    }
}
