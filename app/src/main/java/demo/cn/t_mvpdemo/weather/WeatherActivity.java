package demo.cn.t_mvpdemo.weather;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.BaseActivity;
import demo.cn.t_mvpdemo.ui.widget.loadmoreview.PullLoadMoreRecyclerView;

/**
 * Author  wangchenchen
 * CreateDate 2016/6/29.
 * Email wcc@jusfoun.com
 * Description
 */
public class WeatherActivity extends BaseActivity<WeatherPresenter, WeatherModel>
        implements WeatherContract.View, PullLoadMoreRecyclerView.PullLoadMoreListener {

    private PullLoadMoreRecyclerView weatherList;
    private TextView city;
    private WeatherAdaper adaper;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView() {
        weatherList = (PullLoadMoreRecyclerView) findViewById(R.id.weatherList);
        city = (TextView) findViewById(R.id.city);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initAction() {
        adaper = new WeatherAdaper();
        weatherList.setLinearLayout()
                .setAdapter(adaper)
                .setPushRefreshEnable(false)
                .setOnPullLoadMoreListener(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("cityid", "101010100");
        mPresenter.getWeather(map);
    }

    @Override
    public void getWeatherSuc(WeatherBean weatherBean) {
        weatherList.setPullLoadMoreCompleted();
        adaper.refresh(weatherBean.getRetData().getForecast());
        city.setText(weatherBean.getRetData().getCity());
        Toast.makeText(mContext, weatherBean.getRetData().getCity(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getWeatherFail(String error) {
        weatherList.setPullLoadMoreCompleted();
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
        Log.e(TAG, error);
    }

    @Override
    public void show() {
        showDialog();
    }

    @Override
    public void hide() {
        hideDialog();
    }

    @Override
    public void goActivity(Class cla) {
        Intent intent=new Intent(this,cla);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        HashMap<String, String> map = new HashMap<>();
        map.put("cityid", "101010100");
        mPresenter.getWeather(map);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
