package demo.cn.t_mvpdemo.splash;

import java.io.Serializable;

import demo.cn.t_mvpdemo.base.BaseBean;
import demo.cn.t_mvpdemo.base.BaseEntity;

/**
 * Author  wangchenchen
 * CreateDate 2016/8/26.
 * Email wcc@jusfoun.com
 * Description
 */
public class VersionBean extends BaseBean {
    private VersionNumModel versionnumber;

    public VersionNumModel getVersionnumber() {
        return versionnumber;
    }

    public void setVersionnumber(VersionNumModel versionnumber) {
        this.versionnumber = versionnumber;
    }

    public class VersionNumModel implements Serializable{

        private int update;
        private String describe;
        private String url;
        private int filter;
        private String versionname;
        private String titletext;
        private String cacletext;
        private String updatetext;

        public int getUpdate() {
            return update;
        }

        public void setUpdate(int update) {
            this.update = update;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getFilter() {
            return filter;
        }

        public void setFilter(int filter) {
            this.filter = filter;
        }

        public String getVersionname() {
            return versionname;
        }

        public void setVersionname(String versionname) {
            this.versionname = versionname;
        }

        public String getTitletext() {
            return titletext;
        }

        public void setTitletext(String titletext) {
            this.titletext = titletext;
        }

        public String getCacletext() {
            return cacletext;
        }

        public void setCacletext(String cacletext) {
            this.cacletext = cacletext;
        }

        public String getUpdatetext() {
            return updatetext;
        }

        public void setUpdatetext(String updatetext) {
            this.updatetext = updatetext;
        }
    }

}
