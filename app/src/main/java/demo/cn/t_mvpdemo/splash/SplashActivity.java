package demo.cn.t_mvpdemo.splash;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.BaseActivity;
import demo.cn.t_mvpdemo.ui.widget.Rotate3dAnimation;
import demo.cn.t_mvpdemo.weather.WeatherActivity;
import demo.cn.t_mvpdemo.ui.view.FireView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * Author  wangchenchen
 * CreateDate 2016/6/12.
 * Email wcc@jusfoun.com
 * Description
 */
public class SplashActivity extends BaseActivity {

    private LinearLayout layout;
    private ImageView image;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        layout= (LinearLayout) findViewById(R.id.layout);

        image= (ImageView) findViewById(R.id.image);
        String url="http://img2.3lian.com/2014/f6/173/d/51.jpg";
        Glide.with(this)
                .load(url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.w00)
                .crossFade(300)
                .into(image);

        /*layout.addView(new FireView(mContext), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));*/

        android.os.Handler handler=new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(mContext, WeatherActivity.class);
                startActivity(intent);
                onBackPressed();
            }
        }, 10000000);

        Log.e("center",getWindowManager().getDefaultDisplay().getWidth()/2+"aaaa"+
                getWindowManager().getDefaultDisplay().getHeight()/2);
        Rotate3dAnimation rotate3dAnimation=new Rotate3dAnimation(this.getApplicationContext(),0,360,getWindowManager().getDefaultDisplay().getWidth()/2,
                getWindowManager().getDefaultDisplay().getHeight()/2,0,false);
//        rotate3dAnimation.setRepeatCount(-1);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setDuration(5000);
//        image.setAnimation(rotate3dAnimation);
        image.startAnimation(rotate3dAnimation);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("image","click");
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN)
            Log.e("testactivity","dispatchTouchEvent=="+super.dispatchTouchEvent(ev));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN)
            Log.e("testactivity","onTouchEvent=="+super.onTouchEvent(event));
        return super.onTouchEvent(event);
    }


    @Override
    public void initPresenter() {

    }

    private Observable<ApplicationInfo> getApps(){
        return Observable.create(new Observable.OnSubscribe<ApplicationInfo>() {
            @Override
            public void call(Subscriber<? super ApplicationInfo> subscriber) {

            }
        }).scan(new Func2<ApplicationInfo, ApplicationInfo, ApplicationInfo>() {
            @Override
            public ApplicationInfo call(ApplicationInfo applicationInfo, ApplicationInfo applicationInfo2) {
                return null;
            }
        });
    }
}
