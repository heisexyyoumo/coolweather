package com.example.heiseyoumo.coolweather.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heiseyoumo.coolweather.R;
import com.example.heiseyoumo.coolweather.entity.Forecast;
import com.example.heiseyoumo.coolweather.entity.Weather;
import com.example.heiseyoumo.coolweather.util.JsonUtil;
import com.example.heiseyoumo.coolweather.util.LogUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

public class WeatherActivity extends AppCompatActivity {

    public static final String TAG = "WeatherActivity";

    private ScrollView sv_weather;

    private TextView tv_city;
    private TextView tv_update_time;

    private TextView tv_degree;
    private TextView tv_weather_info;

    private LinearLayout ll_forecast;

    private TextView tv_aqi;
    private TextView tv_pm25;

    private TextView tv_comfort;
    private TextView tv_car_wash;
    private TextView tv_sport;

    //获取bing的每日一图做背景图片
    private ImageView iv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initView();
    }

    //初始化各控件
    private void initView() {

        sv_weather = (ScrollView)findViewById(R.id.sv_weather);

        tv_city = (TextView)findViewById(R.id.tv_city);
        tv_update_time = (TextView)findViewById(R.id.tv_update_time);

        tv_degree = (TextView)findViewById(R.id.tv_degree);
        tv_weather_info = (TextView)findViewById(R.id.tv_weather_info);

        ll_forecast = (LinearLayout)findViewById(R.id.ll_forecast);

        tv_aqi = (TextView)findViewById(R.id.tv_aqi);
        tv_pm25 = (TextView)findViewById(R.id.tv_pm25);

        tv_comfort = (TextView)findViewById(R.id.tv_comfort);
        tv_car_wash = (TextView)findViewById(R.id.tv_car_wash);
        tv_sport = (TextView)findViewById(R.id.tv_sport);

        iv_img = (ImageView)findViewById(R.id.iv_img);

        //获取SharedPreferences的实例
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);

        if (weatherString != null){
            //有缓存直接解析天气数据
            Weather weather = JsonUtil.parsingWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else {
            //无缓存去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            //去服务器请求数据现将ScrollView隐藏，不然空数据的界面看上去不好看
            sv_weather.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(iv_img);
        }else {
            loadBingPic();
        }

    }

    private void loadBingPic() {
        String bingUrl = "http://guolin.tech/api/bing_pic";
        RxVolley.get(bingUrl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                final String bingPic = t;
                SharedPreferences.Editor editor =
                        PreferenceManager.getDefaultSharedPreferences
                                (WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(iv_img);
                    }
                });
            }

        });

    }

    //拼接url
    private void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                "&key=72e91c1c18ea4e7292ca877c58835a90";

        RxVolley.get(weatherUrl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                final String responseText = t;
                final Weather weather = JsonUtil.parsingWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && JsonUtil.mList.size() > 0
                                && weather.getStatus().equals("ok")){
                            //获取SharedPreferences.Editor的实例
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences
                                            (WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,
                                    "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onFailure(VolleyError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loadBingPic();
    }


    /**
     * 将请求得到的数据装入对应的布局中
     */
    private void showWeatherInfo(Weather weather) {

        tv_city.setText(weather.getCityName());
        //时间格式为2018-7-25 13:48 ，下面实现去13:48出来
        tv_update_time.setText(weather.getUpdateTime().split(" ")[1]);

        tv_degree.setText(weather.getDegree() + "°C");
        tv_weather_info.setText(weather.getWeatherInfo());

        tv_aqi.setText(weather.getAqi());
        tv_pm25.setText(weather.getPm25());

        tv_comfort.setText("舒适度：" + weather.getComfort());
        tv_car_wash.setText("洗车指数：" + weather.getCarWash());
        tv_sport.setText("运动建议：" + weather.getSport());

        for (int i = 0; i < JsonUtil.mList.size(); i++){
            View view = LayoutInflater.from(this).inflate
                    (R.layout.forecast_item,ll_forecast,false);

            Forecast forecast = JsonUtil.mList.get(i);

            LogUtil.w(TAG,"date : " + forecast.getForecastDate());
            TextView tv_date = (TextView)view.findViewById(R.id.tv_date);
            TextView tv_info = (TextView)view.findViewById(R.id.tv_info);
            TextView tv_max = (TextView)view.findViewById(R.id.tv_max);
            TextView tv_min = (TextView)view.findViewById(R.id.tv_min);

            tv_date.setText(forecast.getForecastDate());
            tv_info.setText(forecast.getForecastWeather());
            tv_max.setText(forecast.getForecastMax() + "°C");
            tv_min.setText(forecast.getForecastMin() + "°C");

            ll_forecast.addView(view);
        }

        sv_weather.setVisibility(View.VISIBLE);

    }
}
