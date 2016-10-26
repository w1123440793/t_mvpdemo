package demo.cn.t_mvpdemo.weather;

import java.util.HashMap;

import demo.cn.t_mvpdemo.base.BaseModel;
import demo.cn.t_mvpdemo.base.BasePresenter;
import demo.cn.t_mvpdemo.base.BaseView;
import rx.Observable;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/4.
 * Email wcc@jusfoun.com
 * Description
 */
public interface WeatherContract {
    interface Model extends BaseModel{
        public Observable<WeatherBean> getWeather(HashMap<String,String> params);
    }

    interface View extends BaseView {
        void getWeatherSuc(WeatherBean weatherBean);
        void getWeatherFail(String error);
        void show();
        void hide();
        void goActivity(Class cla);
    }

    abstract class Presenter extends BasePresenter<Model, View>{
        public abstract void getWeather(HashMap<String,String> map);
    }
}
