package demo.cn.t_mvpdemo.base;

import java.io.Serializable;
import java.util.Map;

import rx.Observable;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/21.
 * Email wcc@jusfoun.com
 * Description
 */
public interface BaseEntity {

    class BaseBean implements Serializable{
        public long id;
        public String objectId;
        public Map<String,String> params;
        public int type;
    }

    interface IListBean{
        Observable getPageAt(int page);
        void setParam(Map<String,String> params);
    }

    abstract class ListBean extends BaseBean implements IListBean{

        @Override
        public void setParam(Map<String, String> params) {
            this.params=params;
        }
    }
}
