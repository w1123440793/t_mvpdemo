package demo.cn.t_mvpdemo.ui.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.BaseActivity;
import demo.cn.t_mvpdemo.bean.AppInfo;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private ImageView image;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        rxBus.post(new Object());

        image= (ImageView) findViewById(R.id.image);
        Subscriber<String> subscriber=new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                Log.e(TAG,"onstart");
            }

            @Override
            public void onCompleted() {
                Log.e(TAG,"oncompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"onError"+e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG,s);
            }
        };

        Observable observable=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello");
            }
        });

        RxView.clicks(image)
                .throttleFirst(500, TimeUnit.MILLISECONDS);

        ArrayList<ArrayList<String>> map=new ArrayList<>();

        for (int j = 0; j < 3; j++) {
            ArrayList<String> list=new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(i+"");
            }
            map.add(list);
        }

        final String string[]=new String[]{"1","2"};

        Observable.from(map)
                .flatMap(new Func1<ArrayList<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(ArrayList<String> strings) {
                        return Observable.from(strings);
                    }
                }).subscribe(subscriber);

        observable.just(R.mipmap.ic_launcher)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Integer, Bitmap>() {
                    @Override
                    public Bitmap call(Integer integer) {
                        return BitmapFactory.decodeResource(getResources(), integer);
                    }
                }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                image.setImageBitmap(bitmap);
            }
        });

//        observable.subscribe(subscriber);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initAction() {

    }

    private Observable<AppInfo> getApps(){
        return Observable.create(new Observable.OnSubscribe<AppInfo>() {
            @Override
            public void call(Subscriber<? super AppInfo> subscriber) {
                Intent mainIntent=new Intent(Intent.ACTION_MAIN,null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> infos=getPackageManager().queryIntentActivities(mainIntent,0);

                for (ResolveInfo resolveInfo : infos) {
                    Bitmap icon=BitmapFactory.decodeResource(getResources(),resolveInfo.getIconResource());
                    String name=resolveInfo.activityInfo.name;
//                    String iconPath=
                    if (subscriber.isUnsubscribed())
                        return;
                    subscriber.onNext(new AppInfo(name,0,""));
                }
                if (!subscriber.isUnsubscribed())
                    subscriber.onCompleted();
            }
        }).filter(new Func1<AppInfo, Boolean>() {
            @Override
            public Boolean call(AppInfo appInfo) {
                return appInfo.getmName().startsWith("c");
            }
        });
    }

}
