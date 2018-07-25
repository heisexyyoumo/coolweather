package com.example.heiseyoumo.coolweather.util;

import android.util.Log;

public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    //当取NOTHING，即不打印日志
    public static final int NOTHING = 6;
    //修改level的值，就可以自由的控制日志的打印行为
    public static final int level = VERBOSE;

    public static void v(String tag, String msg){
        if (level <= VERBOSE){
            Log.v(tag,msg);
        }
    }

    public static void d(String tag, String msg){
        if (level <= DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag, String msg){
        if (level <= INFO){
            Log.i(tag,msg);
        }
    }

    public static void w(String tag, String msg){
        if (level <= WARN){
            Log.w(tag,msg);
        }
    }

    public static void e(String tag, String msg){
        if (level <= ERROR){
            Log.e(tag,msg);
        }
    }

}