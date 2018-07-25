package com.example.heiseyoumo.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.heiseyoumo.coolweather.ui.WeatherActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //先从sharedpreferences文件中读取缓存数据，如果不为null就说明之前已经请求过数据了
        // 就直接跳转到WeatherActivity中即可
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null) != null){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
