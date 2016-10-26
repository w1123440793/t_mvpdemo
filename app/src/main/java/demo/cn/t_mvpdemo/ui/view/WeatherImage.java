package demo.cn.t_mvpdemo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import demo.cn.t_mvpdemo.R;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/21.
 * Email wcc@jusfoun.com
 * Description
 */
public class WeatherImage extends ImageView {
    public WeatherImage(Context context) {
        super(context);
    }

    public WeatherImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeatherImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setWeatherIcon(String type){
        switch (type){
            case "晴":
                setImageResource(R.mipmap.w00);
                break;
            case "多云":
                setImageResource(R.mipmap.w01);
                break;
            case "阴":
                setImageResource(R.mipmap.w02);
                break;
            case "阵雨":
                setImageResource(R.mipmap.w03);
                break;
            case "雷阵雨":
                setImageResource(R.mipmap.w04);
                break;
            case "雷阵雨伴有冰雹":
                setImageResource(R.mipmap.w05);
                break;
            case "雨夹雪":
                setImageResource(R.mipmap.w06);
                break;
            case "小雨":
                setImageResource(R.mipmap.w07);
                break;
            case "中雨":
                setImageResource(R.mipmap.w08);
                break;
            case "大雨":
                setImageResource(R.mipmap.w09);
                break;
            case "暴雨":
                setImageResource(R.mipmap.w10);
                break;
            case "大暴雨":
                setImageResource(R.mipmap.w11);
                break;
            case "特大暴雨":
                setImageResource(R.mipmap.w12);
                break;
            case "阵雪":
                setImageResource(R.mipmap.w13);
                break;
            case "小雪":
                setImageResource(R.mipmap.w14);
                break;
            case "中雪":
                setImageResource(R.mipmap.w15);
                break;
            case "大雪":
                setImageResource(R.mipmap.w16);
                break;
            case "暴雪":
                setImageResource(R.mipmap.w17);
                break;
            case "雾":
                setImageResource(R.mipmap.w18);
                break;
            case "冻雨":
                setImageResource(R.mipmap.w19);
                break;
            case "沙尘暴":
                setImageResource(R.mipmap.w20);
                break;
            case "小到中雨":
                setImageResource(R.mipmap.w21);
                break;
            case "中到大雨":
                setImageResource(R.mipmap.w22);
                break;
            case "大到暴雨":
                setImageResource(R.mipmap.w23);
                break;
            case "暴雨到大暴雨":
                setImageResource(R.mipmap.w24);
                break;
            case "大暴雨到特大暴雨":
                setImageResource(R.mipmap.w25);
                break;
            case "小到中雪":
                setImageResource(R.mipmap.w26);
                break;
            case "中到大雪":
                setImageResource(R.mipmap.w27);
                break;
            case "大到暴雪":
                setImageResource(R.mipmap.w28);
                break;
            case "浮沉":
                setImageResource(R.mipmap.w29);
                break;
            case "扬沙":
                setImageResource(R.mipmap.w30);
                break;
            case "强沙尘暴":
                setImageResource(R.mipmap.w31);
                break;
            case "霾":
                setImageResource(R.mipmap.w53);
                break;
            default:
                setImageResource(R.mipmap.undefined);
                break;
        }
    }
}
