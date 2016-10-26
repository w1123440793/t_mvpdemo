package demo.cn.t_mvpdemo.bean;

/**
 * Author  wangchenchen
 * CreateDate 2016/8/24.
 * Email wcc@jusfoun.com
 * Description
 */
public class AppInfo implements Comparable<Object> {

    long mLastUpdateTime;
    String mName;
    String mIcon;

    public AppInfo(String mName,long mLastUpdateTime,String mIcon){
        this.mName=mName;
        this.mLastUpdateTime=mLastUpdateTime;
        this.mIcon=mName;
    }
    @Override
    public int compareTo(Object another) {
        AppInfo appInfo= (AppInfo) another;
        return mName.compareTo(appInfo.mName);
    }

    public long getmLastUpdateTime() {
        return mLastUpdateTime;
    }

    public void setmLastUpdateTime(long mLastUpdateTime) {
        this.mLastUpdateTime = mLastUpdateTime;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }
}
