package demo.cn.t_mvpdemo.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.Util.TUtil;
import demo.cn.t_mvpdemo.ui.layout.SwipeBackLayout;
import demo.cn.t_mvpdemo.ui.widget.LoadingDialog;

/**
 * Author  wangchenchen
 * CreateDate 2016/6/12.
 * Email wcc@jusfoun.com
 * Description
 */
public abstract class BaseActivity<T extends BasePresenter,E extends BaseModel> extends AppCompatActivity {

    protected String TAG="";
    private SwipeBackLayout swipeBackLayout;
    private ImageView isShadow;
    public T mPresenter;
    public E mModel;
    public Context mContext;
    protected RxBus rxBus;
    protected LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=getClass().getSimpleName();
        mContext=this;
        initDialog();
        setContentView(getLayoutResId());
        mPresenter= TUtil.getT(getClass(),0);
        mModel=TUtil.getT(getClass(),1);
        rxBus=RxBus.getInstance();
        initView();
        initPresenter();
        initAction();
    }

    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID== R.layout.activity_main||layoutResID==R.layout.activity_splash){
            super.setContentView(layoutResID);
        }else {
            setContentView(getContainer());
            View view= LayoutInflater.from(this).inflate(layoutResID,null);
            swipeBackLayout.addView(view);
        }
    }

    protected void initDialog(){
        if (dialog==null){
            dialog=new LoadingDialog(this,R.style.my_dialog);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_UP)
                    {
                        dialog.cancel();
                    }
                    return true;
                }
            });
        }
    }

    private View getContainer(){
        RelativeLayout container=new RelativeLayout(this);
        swipeBackLayout=new SwipeBackLayout(this);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        isShadow=new ImageView(this);
        isShadow.setBackgroundColor(getResources().getColor(R.color.theme_black_7f));

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(isShadow,params);
        container.addView(swipeBackLayout);
        swipeBackLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                isShadow.setAlpha(1-fractionScreen);
            }
        });
        return container;
    }

    public void showDialog(){
        if (dialog!=null&&!dialog.isShowing()){
            dialog.show();
        }
    }

    public void hideDialog(){
        if (dialog!=null&&dialog.isShowing()){
            dialog.cancel();
        }
    }

    public abstract int getLayoutResId();
    public abstract void initView();
    public abstract void initPresenter();
    public void initAction(){};

}
