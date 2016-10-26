package demo.cn.t_mvpdemo.base.Util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/4.
 * Email wcc@jusfoun.com
 * Description app信息工具类
 */
public class AppUtil {

    // 获取当前应用应用名
    public static String getAppName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    /**
     * 获取程序版本Code
     *
     * @param ct
     * @return
     */
    public static int getVersionCode(Context ct) {
        try {
            PackageManager packageManager = ct.getPackageManager();
            // getPackageName()是当前类的包名，0代表是获取版本信�?
            PackageInfo packInfo = packageManager.getPackageInfo(
                    ct.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 获取渠道号
     */
    public static String getChannelName(Context context) {
        String msg = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 获取程序版本名称?
     *
     * @param ct
     * @return
     */
    public static String getVersionName(Context ct) {
        try {
            PackageManager packageManager = ct.getPackageManager();
            // getPackageName()是当前类的包名，0代表是获取版本信�?
            PackageInfo packInfo = packageManager.getPackageInfo(
                    ct.getPackageName(), 0);
            return packInfo.versionName;
        } catch (Exception e) {
            return "1.1.1";
        }
    }

    /**
     * 签名证书
     *
     * @param ct
     */
    public void getSignInfo(Context ct) {
        try {

            PackageManager packageManager = ct.getPackageManager();
            // getPackageName()是当前类的包名，0代表是获取版本信�?
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    ct.getPackageName(), PackageManager.GET_SIGNATURES);

            // PackageInfo packageInfo = getPackageManager().getPackageInfo(
            // "wabao.et.master", PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            // Signature sign = signs[0];
            // parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取当前应用包名
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    // 获取当前应用图标
    public static Drawable getAppIcon(Context context) {
        return context.getApplicationInfo().loadIcon(context.getPackageManager());
    }

    // 通过进程名获取进程的进程id
    public static int getPid(Context context, String processName) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            if (processName.equals(appProcess.processName)) {
                return appProcess.pid;
            }
        }
        return 0;
    }

    // 安装apk
    public static void installApk(Context context, String apkPath) {
        File file = new File(apkPath);
        Log.e("", "apk size=" + IOUtil.getFileSize(file));
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    // 启动apk
    public static void launchApk(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    // 通过文件名获取包名
    public static String getPackageName(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            return appInfo.packageName;
        } else {
            return null;
        }
    }

    // 判断本程序 最顶部activity 是否是 此activity
    public static boolean isActivityTopStartThisProgram(Context context, String activityName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(100);
        if (list != null && list.size() > 0) {
            for (ActivityManager.RunningTaskInfo runningTaskInfo : list) {
                Log.e("tag",
                        "runningTaskInfo.topActivity.getClassName() = " + runningTaskInfo.topActivity.getClassName());
                Log.e("tag", "activityName = " + activityName);
                if (context.getPackageName().equals(runningTaskInfo.baseActivity.getPackageName())) {
                    if (runningTaskInfo.topActivity.getClassName().equals(activityName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否已经启动这个程序 且在活动在第一个界面
     *
     * @param context
     * @return
     */
    public static boolean isTopStartProgram(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(100);
        if (list != null && list.size() > 0) {
            ActivityManager.RunningTaskInfo runningTaskInfo = list.get(0);
            if (context.getPackageName().equals(runningTaskInfo.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context
     * @return
     */

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean appIsRun(final Context mContext) {
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if(mContext.getPackageName().equals(info.topActivity.getPackageName())||mContext.getPackageName().equals(info.baseActivity.getPackageName()))
                return true;
        }

        return  false;
    }



    public static boolean isAnimatorVision() {
        return android.os.Build.VERSION.SDK_INT >= 11;
    }
}
