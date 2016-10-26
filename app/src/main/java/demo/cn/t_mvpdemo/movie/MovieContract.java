package demo.cn.t_mvpdemo.movie;

import java.util.HashMap;

import demo.cn.t_mvpdemo.base.BaseModel;
import demo.cn.t_mvpdemo.base.BasePresenter;
import demo.cn.t_mvpdemo.base.BaseView;
import demo.cn.t_mvpdemo.weather.WeatherContract;
import rx.Observable;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/27.
 * Email wcc@jusfoun.com
 * Description
 */
public interface MovieContract {

    interface Model extends BaseModel{
        Observable<MovieBean> getMovie(HashMap<String,String> params);
    }

    interface View extends BaseView{
        void hide();
        void show();
        void getMovieSuc(MovieBean bean);
        void getMovieErr(Throwable throwable);
    }

    abstract class Presenter extends BasePresenter<Model,View>{
        public abstract void getMovie(HashMap<String,String> params);
    }
}
