package demo.cn.t_mvpdemo;

import android.app.Application;
import android.content.Context;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/5.
 * Email wcc@jusfoun.com
 * Description
 */
public class App extends Application {

    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
    }

    public static Context getAppContext(){
        return mApp;
    }

    public static String getAppString(int resId){
        return mApp.getString(resId);
    }
}
