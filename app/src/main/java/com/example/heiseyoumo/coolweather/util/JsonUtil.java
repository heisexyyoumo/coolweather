package com.example.heiseyoumo.coolweather.util;

/**
 * 解析和处理服务器返回的json数据
 */
import android.text.TextUtils;

import com.example.heiseyoumo.coolweather.db.City;
import com.example.heiseyoumo.coolweather.db.County;
import com.example.heiseyoumo.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtil {

    /**
     *解析和处理服务器返回的省级数据
     */
    public static boolean parsingProvinceResponse(String response){
        //判断是否为空
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = (JSONObject)jsonArray.get(i);
                    Province province = new Province();
                    //得到省的名字name
                    province.setProvinceName(json.getString("name"));
                    //得到省的代号id
                    province.setProvinceCode(json.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的市级数据
     */

    public static boolean parsingCityResponse(String response, int provinceId){
        //判断是否为空
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = (JSONObject)jsonArray.get(i);
                    City city = new City();
                    //得到市的名字name
                    city.setCityName(json.getString("name"));
                    //得到市的代号id
                    city.setCityCode(json.getInt("id"));
                    //得到市所属的省的id（代号）
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean parsingCountyResponse(String response, int cityId){
        //判断是否为空
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = (JSONObject)jsonArray.get(i);
                    County county = new County();
                    //得到县的名字name
                    county.setCountyName(json.getString("name"));
                    //得到县对应的天气id
                    county.setWeatherId(json.getString("weather_id"));
                    //得到县所属的市的id（代号）
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
