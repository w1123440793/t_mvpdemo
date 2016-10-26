package demo.cn.t_mvpdemo.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import java.util.Observable;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.RxManage;
import demo.cn.t_mvpdemo.ui.view.ProgressBarCircular;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/27.
 * Email wcc@jusfoun.com
 * Description
 */
public class LoadingDialog extends Dialog {

    private Context context;
    private ProgressBarCircular progress;
    private TextView loadTxt;
    public LoadingDialog(Context context) {
        super(context);
        initView(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private void initView(Context context){
        this.context=context;
        setContentView(R.layout.dialog_loading);
        progress= (ProgressBarCircular) findViewById(R.id.progress);
        progress.setBackgroundColor(Color.parseColor("#FF4081"));
        loadTxt= (TextView) findViewById(R.id.load_txt);
    }

    public void setLoadTxt(String string){
        loadTxt.setText(string);
    }

    public void setLoadTxt(int resId){
        loadTxt.setText(context.getString(resId));
    }

    @Override
    public void show() {
        super.show();
        progress.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        progress.dismiss();
    }

    @Override
    public void cancel() {
        super.cancel();
    }
}
