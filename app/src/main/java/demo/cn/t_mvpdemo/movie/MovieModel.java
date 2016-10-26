package demo.cn.t_mvpdemo.movie;

import java.math.MathContext;
import java.util.HashMap;

import demo.cn.t_mvpdemo.api.Api;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/27.
 * Email wcc@jusfoun.com
 * Description
 */
public class MovieModel implements MovieContract.Model {
    @Override
    public Observable<MovieBean> getMovie(HashMap<String, String> params) {
        return Api.getInstance().service.getMovie(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
