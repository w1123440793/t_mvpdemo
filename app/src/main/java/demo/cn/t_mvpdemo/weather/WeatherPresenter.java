package demo.cn.t_mvpdemo.weather;

import android.util.Log;

import java.util.HashMap;

import demo.cn.t_mvpdemo.base.RxBus;
import demo.cn.t_mvpdemo.base.Util.LogUtil;
import demo.cn.t_mvpdemo.movie.MovieActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/4.
 * Email wcc@jusfoun.com
 * Description 天气weatherpresenter
 */
public class WeatherPresenter extends WeatherContract.Presenter {
    private Observable<String> observable;

    @Override
    public void onStart() {

        //通过rxManage管理总线，最为安全，结束时清理所有总线，防止内存泄漏
       /* rxManage.on("12345", new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.goActivity(MovieActivity.class);
            }
        });*/

        //普通模式
        observable=RxBus.getInstance().register("12345");
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtil.e("observer","observer");
                        mView.goActivity(MovieActivity.class);
                    }
                });
        /*RxBus.getInstance().onEvent(observable, new Action1<Object>() {
            @Override
            public void call(Object s) {
                mView.goActivity(MovieActivity.class);
            }
        });*/
    }

    @Override
    public void getWeather(HashMap<String, String> map) {
        mView.show();
        rxManage.add(mModel.getWeather(map).subscribe(new Action1<WeatherBean>() {
            @Override
            public void call(WeatherBean weatherBean) {
                mView.hide();
                mView.getWeatherSuc(weatherBean);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.hide();
                mView.getWeatherFail(throwable.getMessage());
            }
        }));

    }

    @Override
    public void onDestroy() {
        RxBus.getInstance().unRegister("12345",observable);
        super.onDestroy();
    }
}
