package demo.cn.t_mvpdemo.movie;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.HashMap;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.BaseActivity;
import demo.cn.t_mvpdemo.base.Util.LogUtil;
import demo.cn.t_mvpdemo.ui.widget.TRecyclerView;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/27.
 * Email wcc@jusfoun.com
 * Description
 */
public class MovieActivity extends BaseActivity<MoviePresenter,MovieModel> implements MovieContract.View {

    private TRecyclerView recyclerView;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_movie;
    }

    @Override
    public void initView() {
        recyclerView= (TRecyclerView) findViewById(R.id.movietop);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    public void initAction() {
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        super.initAction();
        HashMap<String,String> params=new HashMap<>();
        params.put("start","0");
        params.put("count","10");
        mPresenter.getMovie(params);
    }

    @Override
    public void hide() {
        hideDialog();
    }

    @Override
    public void show() {
        showDialog();
    }

    @Override
    public void getMovieSuc(MovieBean bean) {
        LogUtil.e("movie",bean.getTitle());
    }

    @Override
    public void getMovieErr(Throwable throwable) {
        LogUtil.e("movie",throwable.getMessage());
    }
}
