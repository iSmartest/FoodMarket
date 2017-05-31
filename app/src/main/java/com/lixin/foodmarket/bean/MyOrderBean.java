package com.lixin.foodmarket.bean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyOrderBean {
    public String result;
    public String resultNote;
    public int totalPage;
    public List<orders> orders;

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

    public List<MyOrderBean.orders> getOrders() {
        return orders;
    }

    public void setOrders(List<MyOrderBean.orders> orders) {
        this.orders = orders;
    }

    public class orders{
        public String orderId;
        public String oderTotalPrice;
        public String orderState;//1待付款,2待发货,3待收货,4已完成,5退款售后
        public List<orderCommodity> orderCommodity;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderState() {
            return orderState;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        public String getOderTotalPrice() {
            return oderTotalPrice;
        }

        public void setOderTotalPrice(String oderTotalPrice) {
            this.oderTotalPrice = oderTotalPrice;
        }

        public List<MyOrderBean.orders.orderCommodity> getOrderCommodity() {
            return orderCommodity;
        }

        public void setOrderCommodity(List<MyOrderBean.orders.orderCommodity> orderCommodity) {
            this.orderCommodity = orderCommodity;
        }

        public class orderCommodity{
            public String commodityid;
            public String commodityIcon;
            public String commodityNewPrice;
            public String commodityTitle;
            public String commodityBuyNum;
            public String commodityFirstParam;
            public String commoditySecondParam;
            public String getCommodityid() {
                return commodityid;
            }

            public void setCommodityid(String commodityid) {
                this.commodityid = commodityid;
            }

            public String getCommodityIcon() {
                return commodityIcon;
            }

            public void setCommodityIcon(String commodityIcon) {
                this.commodityIcon = commodityIcon;
            }

            public String getCommodityNewPrice() {
                return commodityNewPrice;
            }

            public void setCommodityNewPrice(String commodityNewPrice) {
                this.commodityNewPrice = commodityNewPrice;
            }

            public String getCommodityTitle() {
                return commodityTitle;
            }

            public void setCommodityTitle(String commodityTitle) {
                this.commodityTitle = commodityTitle;
            }

            public String getCommodityBuyNum() {
                return commodityBuyNum;
            }

            public void setCommodityBuyNum(String commodityBuyNum) {
                this.commodityBuyNum = commodityBuyNum;
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
}
