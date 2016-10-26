package demo.cn.t_mvpdemo.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author  wangchenchen
 * CreateDate 2016/10/12.
 * Email wcc@jusfoun.com
 * Description
 */
public class TestView extends View {
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("test",500,500,new Paint());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN)
        Log.e("testview","dispatchTouchEvent=="+super.dispatchTouchEvent(ev));
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN)
        Log.e("testview","onTouchEvent=="+super.onTouchEvent(event));
        return super.onTouchEvent(event);
    }
}
