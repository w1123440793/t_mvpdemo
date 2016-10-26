package demo.cn.t_mvpdemo.base;

import java.util.HashMap;
import java.util.Map;

import demo.cn.t_mvpdemo.base.Util.LogUtil;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/6.
 * Email wcc@jusfoun.com
 * Description 管理rxbus实践和rxjava 相关代码生命周期
 */
public class RxManage {

    public RxBus mRxbus=RxBus.getInstance();
    private Map<String, Observable<?>> mObservables=new HashMap<>();
    private CompositeSubscription mCompositeSubscription=new CompositeSubscription();

    public void on(String eventName,Action1<Object> action1){
        Observable<?> mObservable=mRxbus.register(eventName);
        mObservables.put(eventName,mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
        .subscribe(action1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtil.e(getClass().getSimpleName(),throwable.getMessage());
            }
        }));
    }

    public void add(Subscription m){
        mCompositeSubscription.add(m);
    }

    public void post(Object tag,Object content){
        mRxbus.post(tag, content);
    }

    public void clear(){
        mCompositeSubscription.unsubscribe();
        for (Map.Entry<String,Observable<?>> entry:mObservables.entrySet()){
            mRxbus.unRegister(entry.getKey(),entry.getValue());
        }
    }
}
