package com.lixin.foodmarket.bean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/25
 * My mailbox is 1403241630@qq.com
 */

public class ShopCartBean {
    public String result;
    public String resultNote;
    public int totalPage;
    public List<shop> shop;

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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ShopCartBean.shop> getShop() {
        return shop;
    }

    public void setShop(List<ShopCartBean.shop> shop) {
        this.shop = shop;
    }

    public class shop{
        public String commodityid;
        public String commodityTitle;
        public String commodityIcon;
        public String commodityFirstParam;
        public String commoditySecondParam;
        public String commodityNewPrice;
        public String commodityShooCarNum;
        public boolean isChoosed;
        public boolean isCheck = false;

        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }

        public String getCommodityTitle() {
            return commodityTitle;
        }

        public void setCommodityTitle(String commodityTitle) {
            this.commodityTitle = commodityTitle;
        }

        public String getCommodityIcon() {
            return commodityIcon;
        }

        public void setCommodityIcon(String commodityIcon) {
            this.commodityIcon = commodityIcon;
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

        public String getCommodityNewPrice() {
            return commodityNewPrice;
        }

        public void setCommodityNewPrice(String commodityNewPrice) {
            this.commodityNewPrice = commodityNewPrice;
        }

        public String getCommodityShooCarNum() {
            return commodityShooCarNum;
        }

        public void setCommodityShooCarNum(String commodityShooCarNum) {
            this.commodityShooCarNum = commodityShooCarNum;
        }
    }
}
