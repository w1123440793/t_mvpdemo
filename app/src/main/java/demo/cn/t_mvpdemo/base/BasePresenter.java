package demo.cn.t_mvpdemo.base;

import android.content.Context;
import android.util.Log;

import rx.subscriptions.CompositeSubscription;

/**
 * Author  wangchenchen
 * CreateDate 2016/6/12.
 * Email wcc@jusfoun.com
 * Description
 */
public abstract class BasePresenter<E,T> {

    private String TAG="";
    public Context context;
    public T mView;
    public E mModel;
    public RxManage rxManage=new RxManage();

    public void setVM(T v,E m){
        this.mModel=m;
        this.mView=v;
        TAG=getClass().getSimpleName();
        onStart();
    }

    public abstract void onStart();

    public void onDestroy(){
        Log.e(TAG,"destroy");
        rxManage.clear();
    }
}
