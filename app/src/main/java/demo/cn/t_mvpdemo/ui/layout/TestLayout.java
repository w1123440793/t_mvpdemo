package demo.cn.t_mvpdemo.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Author  wangchenchen
 * CreateDate 2016/10/12.
 * Email wcc@jusfoun.com
 * Description
 */
public class TestLayout extends LinearLayout {
    public TestLayout(Context context) {
        super(context);
    }

    public TestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN)
        Log.e("testlayout","dispatchTouchEvent=="+super.dispatchTouchEvent(ev));
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN)
        Log.e("testlayout","onTouchEvent=="+super.onTouchEvent(event));
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN)
        Log.e("testlayout","onInterceptTouchEvent=="+super.onInterceptTouchEvent(ev));
        return true;
    }
}
