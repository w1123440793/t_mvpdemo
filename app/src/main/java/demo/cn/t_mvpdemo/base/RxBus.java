package demo.cn.t_mvpdemo.base;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import demo.cn.t_mvpdemo.base.Util.LogUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Author  wangchenchen
 * CreateDate 2016/6/17.
 * Email wcc@jusfoun.com
 * Description
 */
public class RxBus {

    private String TAG=RxBus.class.getSimpleName();

    private final Subject<Object,Object> _bus;

    private ConcurrentMap<Object,List<Subject>> subjectMapper=new ConcurrentHashMap<>();

    /**
     * 饿汉式单例模式
     */
    private static class RxBusHolder{
        private static final RxBus instance=new RxBus();

    }

    public static synchronized RxBus getInstance(){
        return RxBusHolder.instance;
    }

    /**
     * 订阅事件源
     * @param mObservable
     * @param mAction
     */
    public void onEvent(Observable<?> mObservable,Action1<Object> mAction){
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAction, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    };

    public <T> Observable<T> register(@NonNull Object tag){
        List<Subject> subjects=subjectMapper.get(tag);
        if (null==subjects){
            subjects=new ArrayList<>();
            subjectMapper.put(tag,subjects);
        }

        Subject<T,T> subject=PublishSubject.create();
        subjects.add(subject);
        return subject;
    }

    public void unRegister(@NonNull Object tag,Observable<?> mObservable){
        if (null==mObservable)
            return;
        List<Subject> subjects=subjectMapper.get(tag);
        if (subjects!=null) {
            subjects.remove(mObservable);
            if (subjects.isEmpty())
                subjectMapper.remove(tag);
        }
    }

    public void post(@NonNull Object tag,@NonNull Object content){
        LogUtil.e(TAG,"post"+tag);
        List<Subject> subjectList=subjectMapper.get(tag);
        if (subjectList!=null&&subjectList.size()>0){
            for (Subject subject:subjectList){
                subject.onNext(content);
                LogUtil.e(TAG,"eventName=="+tag);
            }
        }
    }
    private RxBus(){
        _bus=new SerializedSubject<>(PublishSubject.create());
    }

    public void post(Object o){
        _bus.onNext(o);
    }

    public <T>Observable<T> toObservable(Class<T> eventType){
        return _bus.ofType(eventType);
    }
}
