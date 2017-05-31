package com.lixin.foodmarket.bean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/23
 * My mailbox is 1403241630@qq.com
 */

public class ClassBean {
    public String result;
    public String resultNote;
    public List<classifyMeun> classifyMeun;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }

    public List<ClassBean.classifyMeun> getClassifyMeun() {
        return classifyMeun;
    }

    public void setClassifyMeun(List<ClassBean.classifyMeun> classifyMeun) {
        this.classifyMeun = classifyMeun;
    }

    public class classifyMeun{
        public String meunType;
        public String commodityIcon;
        public String commodityid;
        public List<meun> meun;

        public String getMeunType() {
            return meunType;
        }

        public void setMeunType(String meunType) {
            this.meunType = meunType;
        }

        public String getCommodityIcon() {
            return commodityIcon;
        }

        public void setCommodityIcon(String commodityIcon) {
            this.commodityIcon = commodityIcon;
        }

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }

        public List<ClassBean.classifyMeun.meun> getMeun() {
            return meun;
        }

        public void setMeun(List<ClassBean.classifyMeun.meun> meun) {
            this.meun = meun;
        }

        public class meun{
            public String meunid;
            public String meunType;
            public String advertisementIcon;

            public String getMeunid() {
                return meunid;
            }

            public void setMeunid(String meunid) {
                this.meunid = meunid;
            }

            public String getAdvertisementIcon() {
                return advertisementIcon;
            }

            public void setAdvertisementIcon(String advertisementIcon) {
                this.advertisementIcon = advertisementIcon;
            }

            public String getMeunType() {
                return meunType;
            }

            public void setMeunType(String meunType) {
                this.meunType = meunType;
            }
        }
    }
}
