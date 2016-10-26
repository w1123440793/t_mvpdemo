package demo.cn.t_mvpdemo.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/21.
 * Email wcc@jusfoun.com
 * Description
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public Context context;
    public BaseViewHolder(View itemView) {
        super(itemView);
        context=itemView.getContext();
    }

    public BaseViewHolder(View itemView,Context context){
        super(itemView);
        this.context=context;
    }

    public abstract int getType();

    public abstract void onBindHolder(View view,T obj);
}
