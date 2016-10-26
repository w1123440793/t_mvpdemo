package demo.cn.t_mvpdemo.base.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/5.
 * Email wcc@jusfoun.com
 * Description
 */
public class NetWorkUtil {

    /**
     * 判断是否连接网络
     * @param context
     * @return
     */
    public static boolean isNetConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
            for (NetworkInfo info:infos){
                if (info.isConnected())
                    return true;
            }
        }

        return false;
    }

    /**
     * 判断是否连接wifi
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
            for (NetworkInfo info:infos){
                if (info.isConnected()&&info.getType()==ConnectivityManager.TYPE_WIFI)
                    return true;
            }
        }

        return false;
    }

    /**
     * 判断是否为移动网络
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
            for (NetworkInfo info:infos){
                if (info.isConnected()&&info.getType()==ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
        }

        return false;
    }
}
