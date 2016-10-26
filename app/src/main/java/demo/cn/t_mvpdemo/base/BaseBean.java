package demo.cn.t_mvpdemo.base;

import java.io.Serializable;

/**
 * Author  wangchenchen
 * CreateDate 2016/8/26.
 * Email wcc@jusfoun.com
 * Description
 */
public abstract class BaseBean implements Serializable {
    private int code;

    private int result;

    private String msg;

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
