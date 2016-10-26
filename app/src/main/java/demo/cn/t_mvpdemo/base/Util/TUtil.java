package demo.cn.t_mvpdemo.base.Util;

import org.greenrobot.greendao.AbstractDaoMaster;

import java.lang.reflect.ParameterizedType;

import javax.xml.validation.Schema;

/**
 * Author  wangchenchen
 * CreateDate 2016/6/12.
 * Email wcc@jusfoun.com
 * Description
 */
public class TUtil {

    public static <T> T getT(Class<?> c, int position) {
        try {

            ParameterizedType type = (ParameterizedType) (c.getGenericSuperclass());
            Class<T> t = (Class<T>) type.getActualTypeArguments()[position];
            return t.newInstance();

            /*return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[position])
                    .newInstance();*/
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (ClassCastException e){
            e.printStackTrace();
        }

        return null;
    }

    public static Class<?> forName(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void aa(){
    }
}
