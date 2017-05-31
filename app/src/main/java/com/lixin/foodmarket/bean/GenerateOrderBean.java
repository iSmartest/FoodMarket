package com.lixin.foodmarket.bean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/26
 * My mailbox is 1403241630@qq.com
 */

public class GenerateOrderBean {
    public String cmd;
    public String uid;
    public List<commoditys> commoditys;
    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public List<GenerateOrderBean.commoditys> getCommoditys() {
        return commoditys;
    }
    public void setCommoditys(List<GenerateOrderBean.commoditys> commoditys) {
        this.commoditys = commoditys;
    }

    public GenerateOrderBean(String cmd,String uid,List<GenerateOrderBean.commoditys> list) {
        this.cmd = cmd;
        this.uid = uid;
        this.commoditys = list;
    }
    public static class commoditys{
        public String commodityid;
        public String buyNum;
        public String commodityFirstParam;
        public String commoditySecondParam;

        public commoditys(String commodityid, String commodityShooCarNum, String commodityFirstParam, String commoditySecondParam) {
            this.commodityid = commodityid;
            this.buyNum = commodityShooCarNum;
            this.commodityFirstParam = commodityFirstParam;
            this.commoditySecondParam = commoditySecondParam;
        }

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }

        public String getBuyNum() {
            return buyNum;
        }

        public void setBuyNum(String buyNum) {
            this.buyNum = buyNum;
        }

        public String getCommodityFirstParam() {
            return commodityFirstParam;
        }

        public void setCommodityFirstParam(String commodityFirstParam) {
            this.commodityFirstParam = commodityFirstParam;
        }

        public String getCommoditySecondParam() {
            return commoditySecondParam;
        }

        public void setCommoditySecondParam(String commoditySecondParam) {
            this.commoditySecondParam = commoditySecondParam;
        }
    }
}
