package demo.cn.t_mvpdemo.base.Util;

import android.util.Log;

import demo.cn.t_mvpdemo.BuildConfig;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/6.
 * Email wcc@jusfoun.com
 * Description
 */
public class LogUtil {

    public static void d(String tag,String msg){
        if (BuildConfig.DEBUG)
            Log.d(tag,msg);
    }

    public static void w(String tag,String msg){
        if (BuildConfig.DEBUG)
            Log.w(tag,msg);
    }

    public static void e(String tag,String msg){
        if (BuildConfig.DEBUG)
            Log.e(tag,msg);
    }

    public static void i(String tag,String msg){
        if (BuildConfig.DEBUG)
            Log.i(tag,msg);
    }
}
