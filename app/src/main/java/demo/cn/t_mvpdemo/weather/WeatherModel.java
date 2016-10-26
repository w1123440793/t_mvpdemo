package demo.cn.t_mvpdemo.weather;

import java.util.HashMap;

import demo.cn.t_mvpdemo.api.Api;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/5.
 * Email wcc@jusfoun.com
 * Description
 */
public class WeatherModel implements WeatherContract.Model {
    @Override
    public Observable<WeatherBean> getWeather(HashMap<String, String> params) {

        return Api.getInstance().service.getWeather(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
