package demo.cn.t_mvpdemo.weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.RxBus;
import demo.cn.t_mvpdemo.ui.view.WeatherImage;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/21.
 * Email wcc@jusfoun.com
 * Description
 */
public class WeatherAdaper extends RecyclerView.Adapter<WeatherAdaper.WeatherHolder> {

    private List<WeatherBean.RetDataBean.ForecastBean> list=new ArrayList<>();

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WeatherHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather,null));
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        holder.update(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(List<WeatherBean.RetDataBean.ForecastBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class WeatherHolder extends RecyclerView.ViewHolder{

        public TextView type,week,temp;
        private WeatherImage image;
        private View itemView;
        public WeatherHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            type = (TextView) itemView.findViewById(R.id.type);
            week = (TextView) itemView.findViewById(R.id.week);
            temp = (TextView) itemView.findViewById(R.id.temp);
            image= (WeatherImage) itemView.findViewById(R.id.image);
        }

        public void update(WeatherBean.RetDataBean.ForecastBean bean){
            type.setText(bean.getType());
            week.setText(bean.getWeek());
            temp.setText(bean.getLowtemp()+"~"+bean.getHightemp());
            image.setWeatherIcon(bean.getType());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxBus.getInstance().post("12345",new String("12345"));
                }
            });

        }
    }
}
