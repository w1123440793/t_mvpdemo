package demo.cn.t_mvpdemo.service.interceptor;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.net.URLDecoder;

import demo.cn.t_mvpdemo.App;
import demo.cn.t_mvpdemo.R;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Author  wangchenchen
 * CreateDate 2016/6/29.
 * Email wcc@jusfoun.com
 * Description
 */
public class HeaderWeather implements Interceptor {
    private String APIKEY="apikey";

    public HeaderWeather(){

    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request().newBuilder()
                .addHeader(APIKEY, App.getAppString(R.string.key_apistore))
                .build();
        String requestContent=bodyToString(request);
        Log.e("request","url=="+request.url().toString());
        long t1=System.nanoTime();
        Response response=chain.proceed(request);
        long t2=System.nanoTime();
        Log.e("response","time=="+((t2-t1)/1e6d));
        if (response.body()!=null)
        {
            Headers headers=response.headers();
            /*if (headers.get("Content-Type")!=null){
                String charset=headers.get("Content-Type");
                if (charset.contains("charset")){
                    int i=charset.indexOf("charset=");
                    charset=charset.substring(i+"charset=".length(),charset.length());
//                    String string=URLDecoder.decode(bodyToString(response.body().string()),charset);
                }
            }*/
//            response.request().headers().get("text/html; charset=GBK");
            ResponseBody responseBody=ResponseBody.create(response.body().contentType(),response.body().string());
            response=response.newBuilder().body(responseBody).build();
//            Log.e("response",response.body().string());
        }
        return response;
    }

    private static String bodyToString(final Request request) {
        try {
            if (request.body() == null) {
                return "(no body)";
            }
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            String requestContent = buffer.readUtf8();
            if (requestContent.length() == 0) {
                requestContent = "(empty body)";
            }
            return requestContent;
        } catch (final Throwable e) {
            return "(body not printable)";
        }
    }

    private static String bodyToString(String responseContent) {
        if (responseContent == null) {
            responseContent = "(no body)";
        } else if (responseContent.length() == 0) {
            responseContent = "(empty body)";
        }
        return responseContent;
    }
}
