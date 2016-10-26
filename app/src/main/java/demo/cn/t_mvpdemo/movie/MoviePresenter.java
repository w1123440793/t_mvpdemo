package demo.cn.t_mvpdemo.movie;

import java.util.HashMap;

import rx.functions.Action1;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/27.
 * Email wcc@jusfoun.com
 * Description
 */
public class MoviePresenter extends MovieContract.Presenter {
    @Override
    public void getMovie(HashMap<String, String> params) {
        mView.show();
        mModel.getMovie(params).subscribe(new Action1<MovieBean>() {
            @Override
            public void call(MovieBean bean) {
                mView.hide();
                mView.getMovieSuc(bean);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.getMovieErr(throwable);
                mView.hide();
            }
        });
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
