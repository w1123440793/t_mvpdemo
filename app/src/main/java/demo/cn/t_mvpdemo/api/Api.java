package demo.cn.t_mvpdemo.api;

import java.io.File;
import java.util.concurrent.TimeUnit;

import demo.cn.t_mvpdemo.App;
import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.service.interceptor.CacheInterceptor;
import demo.cn.t_mvpdemo.service.interceptor.HeaderWeather;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/5.
 * Email wcc@jusfoun.com
 * Description
 */
public class Api {

    private int TIMEOUT = 10000;
    private OkHttpClient okHttpClient;

    public Retrofit retrofit;
    public ApiService service;

    private static class SingletonHolder {
        private static final Api INSTANCE = new Api();
    }

    public static Api getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Api() {

        File cacheFile = new File(App.getAppContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 10248 * 100);//100Mb
        //设置证书
        CertificatePinner.Builder builder=new CertificatePinner.Builder();
        builder.add("sbbic.com","sha1");
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderWeather())
                .addInterceptor(new CacheInterceptor())
                //https证书锁定
                .certificatePinner(builder.build())
                .cache(cache)
                .build();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(App.getAppString(R.string.url))
                .build();

        service = retrofit.create(ApiService.class);
    }
}
