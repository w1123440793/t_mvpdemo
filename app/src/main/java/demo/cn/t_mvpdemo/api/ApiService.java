package demo.cn.t_mvpdemo.api;

import java.util.HashMap;

import demo.cn.t_mvpdemo.movie.MovieBean;
import demo.cn.t_mvpdemo.splash.VersionBean;
import demo.cn.t_mvpdemo.weather.WeatherBean;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/5.
 * Email wcc@jusfoun.com
 * Description 访问地址api
 */
public interface ApiService {

    @GET("http://apis.baidu.com/apistore/weatherservice/recentweathers")
    Observable<WeatherBean> getWeather(@QueryMap HashMap<String,String> params);

    @GET("http://api.douban.com/v2/movie/top250")
    Observable<MovieBean> getMovie(@QueryMap HashMap<String,String> params);

    @GET("api/VersionNumber/GetVersionNumber")
    Observable<VersionBean> checkVersion(@QueryMap HashMap<String,String> params);
}
