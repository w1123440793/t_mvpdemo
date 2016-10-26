package demo.cn.t_mvpdemo.ui.view.viewholder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.BaseViewHolder;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/22.
 * Email wcc@jusfoun.com
 * Description
 */
public class ListFooterVH extends BaseViewHolder<Object> {

    public ProgressBar progressbar;
    public TextView tv_state;
    public static final int LAYOUT_TYPE = R.layout.list_footer_view;
    public ListFooterVH(View itemView) {
        super(itemView);
    }

    @Override
    public int getType() {
        return LAYOUT_TYPE;
    }

    @Override
    public void onBindHolder(View view, Object obj) {
        boolean isHasMore = (obj == null ? false : true);
        progressbar.setVisibility(isHasMore ? View.VISIBLE : View.GONE);
        tv_state.setText(isHasMore ? "正在加载" : "已经到底");
    }
}
